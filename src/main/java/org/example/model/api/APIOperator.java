package org.example.model.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.example.model.data.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class APIOperator {

    protected static ApiDataResult getData(ApiDataRequest request)
            throws InterruptedException, IOException, ParserConfigurationException, SAXException {
        if (request.getDataClass() == WeatherModel.class) {
            ArrayList<String> values = new ArrayList<>();
            DataRequest dataRequest = request.getDataRequest();
            URL url = new URL(
                    "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::multipointcoverage&place="
                            + dataRequest.getLocation() + "&parameters=" + dataRequest.getDataType() + "&starttime="
                            + dataRequest.getStarttime() + "&endtime=" + dataRequest.getEndtime() + "&timestep=60");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();

            Document document = builder.parse(connection.getInputStream());

            document.getDocumentElement().normalize();

            NodeList data = document.getElementsByTagName("gml:doubleOrNilReasonTupleList");

            System.out.println(data);
            NodeList subData = data.item(0).getChildNodes();

            for (int i = 0; i < subData.getLength(); ++i) {
                values.add(subData.item(i).getNodeValue());
            }

        } else if (request.getDataClass() == EnergyModel.class) {
            // Tästä voidaan päätellä, että kutsutaan Fingridin APIa @HEIKKI
        }

        return new ApiDataResult(null, request);
    }
}
