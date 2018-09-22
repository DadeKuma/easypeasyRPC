package com.github.dadekuma.easypeasyrpc;

import com.github.dadekuma.easypeasyrpc.exception.ParameterOutOfBoundException;
import com.github.dadekuma.easypeasyrpc.exception.RpcException;
import com.github.dadekuma.easypeasyrpc.resource.RpcResponse;
import com.github.dadekuma.easypeasyrpc.resource.error.RpcError;
import com.github.dadekuma.easypeasyrpc.resource.error.RpcErrorType;
import com.github.dadekuma.easypeasyrpc.resource.method.RpcMethodList;
import com.github.dadekuma.easypeasyrpc.resource.method.RpcMethodPerformer;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcElement;
import com.github.dadekuma.easypeasyrpc.resource.params.RpcParameterList;
import com.github.dadekuma.easypeasyrpc.serialization.ErrorSerializer;
import com.github.dadekuma.easypeasyrpc.serialization.ResponseSerializer;
import com.google.gson.*;

public class RpcManager {
    public static final String RPC_VERSION = "2.0";
    private final Gson gson;
    private JsonParser parser;
    private RpcMethodPerformer performer;
    private RpcMethodList methodList;

    public RpcManager(RpcMethodPerformer performer) {
        this(performer, new RpcMethodList());
    }

    public RpcManager(RpcMethodPerformer performer, RpcMethodList methodList) {
        this.performer = performer;
        this.methodList = methodList;
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(RpcResponse.class, new ResponseSerializer())
                .registerTypeAdapter(RpcError.class, new ErrorSerializer())
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
                RpcResponse response = new RpcResponse(RpcErrorType.INVALID_REQUEST,null, RPC_VERSION);
                return gson.toJsonTree(response);
            }
            jsonElement = parser.parse(stringRequest);
        }
        catch (JsonSyntaxException ex){
            RpcResponse response = new RpcResponse(RpcErrorType.PARSE_ERROR,null, RPC_VERSION);
            return gson.toJsonTree(response);
        }
        catch (IllegalStateException e){
            RpcResponse response = new RpcResponse(RpcErrorType.INVALID_REQUEST,null, RPC_VERSION);
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
            return gson.toJsonTree(new RpcResponse(RpcErrorType.INVALID_REQUEST, null, RPC_VERSION));
        }
        JsonArray responses = new JsonArray();

        for(JsonElement element : jsonArray){
            //bad element in array: add response to invalid request
            if(!element.isJsonObject())
                responses.add(gson.toJsonTree(new RpcResponse(RpcErrorType.INVALID_REQUEST, null, RPC_VERSION)));
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
            com.github.dadekuma.easypeasyrpc.resource.RpcRequest request = decodeRequest(jsonObject);
            if(!request.isNotification()){
                RpcResponse response = compileResponse(request);
                return gson.toJsonTree(response);
            }
        }
        catch (RpcException e) {
            RpcResponse response = new RpcResponse(e.getErrorType(),null, RPC_VERSION);
            return gson.toJsonTree(response);
        }
        return null;
    }

    private com.github.dadekuma.easypeasyrpc.resource.RpcRequest decodeRequest(JsonObject jsonObject) throws RpcException {
        try {
            com.github.dadekuma.easypeasyrpc.resource.RpcRequest request = gson.fromJson(jsonObject, com.github.dadekuma.easypeasyrpc.resource.RpcRequest.class);
            if(request.getJsonRPC() == null || !request.getJsonRPC().equals("2.0"))
                throw new RpcException(RpcErrorType.INVALID_REQUEST);
            return request;
        }
        catch (JsonParseException e){
            throw new RpcException(RpcErrorType.PARSE_ERROR, e);
        }
    }

    private RpcResponse compileResponse(com.github.dadekuma.easypeasyrpc.resource.RpcRequest request){
        String requestMethod = request.getMethod();
        RpcParameterList params = request.getParams();
        //method not found
        if(requestMethod == null || requestMethod.isEmpty())
            return new RpcResponse(RpcErrorType.INVALID_REQUEST, null, RPC_VERSION);
        if(!methodList.getMethods().containsKey(requestMethod))
            return new RpcResponse(RpcErrorType.METHOD_NOT_FOUND, request.getId(), RPC_VERSION);
        Integer[] methodParams = methodList.getMethods().get(requestMethod);
        boolean methodHasZeroParams = false, hasSameParams = false;
        //check if invoked method has a number of parameters compatible
        //with RpcMethodList's method
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
                return new RpcResponse(RpcErrorType.INVALID_PARAMS, request.getId(), RPC_VERSION);
            RpcElement result = performer.perform(requestMethod, params);
            return new RpcResponse(result, request.getId(), RPC_VERSION);
        }
        catch(NullPointerException | UnsupportedOperationException | IndexOutOfBoundsException | NumberFormatException | JsonSyntaxException e){
            return new RpcResponse(RpcErrorType.INVALID_PARAMS, request.getId(), RPC_VERSION);
        }
        catch (ParameterOutOfBoundException e){
            e.printStackTrace();
            return new RpcResponse(RpcErrorType.INTERNAL_ERROR, request.getId(), RPC_VERSION);
        }
        catch (RpcException e){
            return new RpcResponse(e.getErrorType(), request.getId(), RPC_VERSION);
        }
    }

    public RpcMethodList getMethodList() {
        return methodList;
    }

    public void setMethodList(RpcMethodList methodList) {
        this.methodList = methodList;
    }
}