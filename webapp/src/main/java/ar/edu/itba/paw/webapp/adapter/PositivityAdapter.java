package ar.edu.itba.paw.webapp.adapter;


import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;

public class PositivityAdapter extends XmlAdapter<PositivityAdapter.PositivityDto, HashMap<String, String>> {

    @XmlRootElement
    public static class PositivityDto {

        public double upvoted;
        public long interactions;
        public String positivity;

    }


    @Override
    public HashMap<String,String> unmarshal(PositivityDto p) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("upvoted", Double.toString(p.upvoted));
        map.put("interactions", Long.toString(p.interactions));
        map.put("positivity", p.positivity);
        return map;
    }


    @Override
    public PositivityDto marshal(HashMap<String, String> v) throws Exception {
        PositivityDto p = new PositivityDto();
        p.upvoted = Double.parseDouble(v.get("upvoted"));
        p.interactions = Long.parseLong(v.get("interactions"));
        p.positivity = (String)v.get("positivity");
        return p;
    }
}