package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RPCException;
import com.github.dadekuma.easypeasyrpc.resource.Request;
import com.github.dadekuma.easypeasyrpc.resource.Response;
import com.github.dadekuma.easypeasyrpc.resource.error.Error;
import com.github.dadekuma.easypeasyrpc.resource.error.ErrorType;
import com.github.dadekuma.easypeasyrpc.resource.method.MethodList;
import com.github.dadekuma.easypeasyrpc.resource.method.MethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.Element;
import com.github.dadekuma.easypeasyrpc.resource.params.ParameterList;
import com.github.dadekuma.easypeasyrpc.serialization.ErrorSerializer;
import com.github.dadekuma.easypeasyrpc.serialization.ResponseSerializer;
import com.google.gson.*;

public class JsonRPCManager {
    public static final String RPC_VERSION = "2.0";
    private final Gson gson;
    private JsonParser parser;
    private MethodPerformer performer;
    private MethodList methodList;

    public JsonRPCManager(MethodPerformer performer) {
        this(performer, new MethodList());
    }

    public JsonRPCManager(MethodPerformer performer, MethodList methodList) {
        this.performer = performer;
        this.methodList = methodList;
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Response.class, new ResponseSerializer())
                .registerTypeAdapter(Error.class, new ErrorSerializer())
                .create();
        parser = new JsonParser();
    }

    public String buildResponseToString(String stringRequest){
        JsonElement jsonResponse = parseRequest(stringRequest);
        return gson.toJson(jsonResponse);
    }

    public JsonElement parseRequest(String stringRequest){
        JsonElement jsonElement;
        try{
            if(stringRequest == null || stringRequest.isEmpty()) {
                Response response = new Response(ErrorType.INVALID_REQUEST,null, RPC_VERSION);
                return gson.toJsonTree(response);
            }
            jsonElement = parser.parse(stringRequest);
        }
        catch (JsonSyntaxException ex){
            Response response = new Response(ErrorType.PARSE_ERROR,null, RPC_VERSION);
            return gson.toJsonTree(response);
        }
        catch (IllegalStateException e){
            Response response = new Response(ErrorType.INVALID_REQUEST,null, RPC_VERSION);
            return gson.toJsonTree(response);
        }
        //batch request
        if(jsonElement.isJsonArray()){
            return parseBatchRequest(jsonElement);
        }
        //single request
        else{
            return parseSingleRequest(jsonElement);
        }
    }

    private JsonElement parseBatchRequest(JsonElement batchRequest){
        JsonArray jsonArray = batchRequest.getAsJsonArray();

        //empty array: invalid request
        if(jsonArray.size() < 1){
            return gson.toJsonTree(new Response(ErrorType.INVALID_REQUEST, null, RPC_VERSION));
        }
        JsonArray responses = new JsonArray();

        for(JsonElement element : jsonArray){
            //bad element in array: add response to invalid request
            if(!element.isJsonObject())
                responses.add(gson.toJsonTree(new Response(ErrorType.INVALID_REQUEST, null, RPC_VERSION)));
            else{
                JsonElement jsonElement = parseSingleRequest(element);
                if(jsonElement != null)
                    responses.add(jsonElement);
            }
        }
        return responses;
    }

    private JsonElement parseSingleRequest(JsonElement singleRequest){
        JsonObject jsonObject = singleRequest.getAsJsonObject();
        try {
            Request request = decodeRequest(jsonObject);
            if(!request.isNotification()){
                Response response = compileResponse(request);
                return gson.toJsonTree(response);
            }
        }
        catch (RPCException e) {
            Response response = new Response(e.getErrorType(),null, RPC_VERSION);
            return gson.toJsonTree(response);
        }
        return null;
    }

    private Request decodeRequest(JsonObject jsonObject) throws RPCException{
        try {
            Request request = gson.fromJson(jsonObject, Request.class);
            if(request.getJsonRPC() == null || !request.getJsonRPC().equals("2.0"))
                throw new RPCException(ErrorType.INVALID_REQUEST);
            return request;
        }
        catch (JsonParseException e){
            throw new RPCException(ErrorType.PARSE_ERROR, e);
        }
    }

    private Response compileResponse(Request request){
        String requestMethod = request.getMethod();
        ParameterList params = request.getParams();
        //method not found
        if(requestMethod == null || requestMethod.isEmpty())
            return new Response(ErrorType.INVALID_REQUEST, null, RPC_VERSION);
        if(!methodList.getMethods().containsKey(requestMethod))
            return new Response(ErrorType.METHOD_NOT_FOUND, request.getId(), RPC_VERSION);
        Integer[] methodParams = methodList.getMethods().get(requestMethod);
        boolean methodHasZeroParams = false, hasSameParams = false;
        //check if invoked method has a number of parameters compatible
        //with methodList's method
        try{
            for (Integer i : methodParams){
                if(i == 0){
                    methodHasZeroParams = true;
                }

                if((params != null && params.getParameters() != null && params.getParameters().size() == i)
                        || params == null && methodHasZeroParams){
                    hasSameParams = true;
                }

            }
            if((params == null && !methodHasZeroParams) || !hasSameParams)
                return new Response(ErrorType.INVALID_PARAMS, request.getId(), RPC_VERSION);
            Element result = performer.perform(requestMethod, params);
            return new Response(result, request.getId(), RPC_VERSION);
        }
        catch(NullPointerException | UnsupportedOperationException | IndexOutOfBoundsException | NumberFormatException | JsonSyntaxException e){
            return new Response(ErrorType.INVALID_PARAMS, request.getId(), RPC_VERSION);
        }
        catch (ParameterOutOfBoundException e){
            e.printStackTrace();
            return new Response(ErrorType.INTERNAL_ERROR, request.getId(), RPC_VERSION);
        }
        catch (RPCException e){
            return new Response(e.getErrorType(), request.getId(), RPC_VERSION);
        }
    }

    public MethodList getMethodList() {
        return methodList;
    }

    public void setMethodList(MethodList methodList) {
        this.methodList = methodList;
    }
}