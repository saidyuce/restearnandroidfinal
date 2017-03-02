package com.theoc.restapp.dataorganization;

import com.theoc.restapp.RegisterActivity;
import com.theoc.restapp.extendclass.DataConnections.ServConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class Kayit extends ServConnection {

private RegisterActivity register;
    private JSONObject  jj_temp;
    public Kayit (RegisterActivity r){
        register=r;

    }

public void kayit_ol(String username,String password,String device_id,String isim,String soyisim){

    jj_temp =new JSONObject();
    try {
        jj_temp.put("user_name",username);
        jj_temp.put("password",password);
        jj_temp.put("device_id",device_id);
        jj_temp.put("type","normal");
        jj_temp.put("isim",isim);
        jj_temp.put("soyisim",soyisim);
        send(jj_temp);
    } catch (JSONException e) {
        e.printStackTrace();
    }
}


    @Override
    protected void onfinish(){

        String state="";

        try {
            state=response_json.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (state) {
            case "yok":

                register.kayit_bitti(KayitType.yok);
                break;
            case "var":
                register.kayit_bitti(KayitType.var);
                break;
            case "fail":
                register.kayit_bitti(KayitType.fail);
                break;
            default:
                register.kayit_bitti(KayitType.serversorunu);
                break;
        }


    }
    @Override
    protected void onError(error e){



    }

    @Override
    protected void onprocessing(){



    }

    private void send(JSONObject j){

        super.adj_parameters_json_normal_mode(j);
        super.onStart(input_type.JSON_IMPUT,output_type.JSONOUT,"kayit.php");
    }





}
