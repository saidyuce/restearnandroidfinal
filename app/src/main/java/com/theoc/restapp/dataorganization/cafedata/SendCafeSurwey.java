package com.theoc.restapp.dataorganization.cafedata;

import com.theoc.restapp.extendclass.DataConnections.ServConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendCafeSurwey extends ServConnection {

    private Map<Integer,Integer> cafesurwey;

    public SendCafeSurwey(){

        cafesurwey=new HashMap<>();
    }

    @Override
    protected  void adj_parameter_json_async() {


        super.sending_parameter=new JSONObject();


        if (!cafesurwey.keySet().isEmpty()){
            JSONArray jsonArray=new JSONArray();
            for (int  b:cafesurwey.keySet() ){
                JSONObject rowObject = new JSONObject();
                try {


                    rowObject.put("id",b+"");
                    rowObject.put("resp",cafesurwey.get(b)+"");


                    jsonArray.put(rowObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            try {
                super.sending_parameter.put("surwey",jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }


    public void send_surwey(){


        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"send_surwey.php");
    }


    public void destroy(){

        super.destroy();
    }


    @Override
    protected void onfinish(){


    }
    @Override
    protected void canceled_task(){

    }
    @Override
    protected void onError(ServConnection.error e){



    }
    @Override
    protected void parser_to_object() {
//super.response_json
// use parser response string or parse response json
        // output response object

    }

    public void set_surweyresponse(int q_id,int res_indis){
        cafesurwey.put(q_id,res_indis);
    }




}
