package org.example.model.data;

public class ApiDataRequest
{
    private final Class dataClass;
    private DataRequest dataRequest;
    public ApiDataRequest(Class dataClass, DataRequest dataRequest)
    {
        this.dataClass = dataClass;
        this.dataRequest = dataRequest;
    }


    public Class getDataClass() {
        return dataClass;
    }

    public DataRequest getDataRequest() {
        return dataRequest;
    }
}
