import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EnumToGuavaMap {

    static Multimap <String, Keyword> map = ArrayListMultimap.create();

    public static void loadData(){
        for(Keyword entry: Keyword.values()){
            Keyword copy = Keyword.valueOf(entry.name());
            map.put(copy.name(), copy);
        }//foreach
    }

}
