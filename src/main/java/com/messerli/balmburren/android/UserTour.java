package com.messerli.balmburren.android;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import java.util.*;


public class UserTour {
//    public static final String jsonString = "{'0': {'name': 'Bernhard Messerli', 'geopoint': '46.972900, 7.341600', 'milk': '0', 'isdelivered': '0', 'address': 'Illiswilstrasse 11, 3033 Wohlen', 'position': '0', 'text': 'Hellohello'}, '1': {'name':'Katja Burren', 'address': 'Mengestorfbergstrasse,3144 Gasel', 'geopoint': '46.899260, 7.386380', 'milk': '2', 'isdelivered': '0', 'position': '1', 'text': ''},'2': {'name': 'Kurt Müller', 'geopoint': '46.982900, 7.351600', 'milk': '2', 'isdelivered': '0', 'address': 'Eriswilstrasse 12, 3048 Wohlen', 'position': '2', 'text': ''}, '3': {'name':'Sandra Müller', 'address': 'Könizstrasse,3140 Köniz', 'geopoint': '46.839260, 7.376380', 'milk': '2', 'isdelivered': '0', 'position': '3', 'text': ''},'4': {'name': 'Eveline Binder', 'geopoint': '46.922900, 7.341600', 'milk': '2', 'isdelivered': '0', 'address': 'Illiswilstrasse 11, 3033 Wohlen', 'position': '4', 'text': 'Hellohello'}, '5': {'name':'David Berger', 'address': 'Mengestorfbergstrasse,3144 Gasel', 'geopoint': '46.869260, 7.386380', 'milk': '2', 'isdelivered': '0', 'position': '5', 'text': ''}, '6': {'name':'Kurt Burren', 'address': 'Mengestorfbergstrasse,3144 Gasel', 'geopoint': '46.869260, 7.386380', 'milk': '2', 'isdelivered': '0', 'position': '6', 'text': ''}, '7': {'name': 'Bernhard Muster', 'geopoint': '46.272900, 7.381600', 'milk': '5', 'isdelivered': '0', 'address': 'Bernstrasse 11, 3000 Berm', 'position': '7', 'text': ''}, '8': {'name':'Carol Bruderer', 'address': 'Wabernstrasse 9, 3000 Bern', 'geopoint': '46.799260, 7.686380', 'milk': '2', 'isdelivered': '0', 'position': '8', 'text': ''},'9': {'name': 'Heinz Müller', 'geopoint': '46.082900, 7.051600', 'milk': '1', 'isdelivered': '0', 'address': 'Wabernstrasse 12, 3000 Bern', 'position': '9', 'text': ''}, '10': {'name':'Sandra Matter', 'address': 'Könizstrasse, 3140 Köniz', 'geopoint': '46.832260, 7.376980', 'milk': '2', 'isdelivered': '0', 'position': '10', 'text': ''},'11': {'name': 'Eveline Bindacher', 'geopoint': '46.722900, 7.941600', 'milk': '2', 'isdelivered': '0', 'address': 'Wabernstrasse 11, 3000 Bern', 'position': '11', 'text': ''}, '12': {'name':'David Blaser', 'address': 'Bergstrasse, 3144 Gasel', 'geopoint': '46.669260, 7.686380', 'milk': '2', 'isdelivered': '0', 'position': '12', 'text': ''}, '13': {'name':'Kevin Balsiger', 'address': 'Murtenstrasse,3000 Bern', 'geopoint': '46.769260, 7.586380', 'milk': '1', 'isdelivered': '0', 'position': '13', 'text': ''}}";
//    private static HashMap<String, Client> clients = new LinkedHashMap<>();

    public UserTour(){};

//    public HashMap<String,Client> getFields(String jsonStr){
        public List<Client> getFields(String jsonStr) throws Exception {

//        HashMap<String,Client> out = new LinkedHashMap<>();
            List<Client> list = new ArrayList<>();
        try{
            JSONObject jo = new JSONObject(jsonStr);

            Iterator<String> keys= jo.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                ObjectMapper mapper = new ObjectMapper();
                Client client = null;

                try {
                    client = mapper.readValue(jo.get(key).toString(), Client.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                out.put(key, client);
                list.add(client);
            }
        }catch(Exception ex){
//            out.put("Error", null);
            throw new Exception(ex);
        }
//        return out;
            return list;
    }


    public String generateJson(Client[] clients) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();


        HashMap<String, Client> out = new LinkedHashMap<>();
        int i = 0;

        for (Client client: clients) {
//            out.put(String.valueOf(i), mapper.writeValueAsString(client));
            out.put(String.valueOf(i), client);
            i++;
        }
        return mapper.writeValueAsString(out);
    }
}
