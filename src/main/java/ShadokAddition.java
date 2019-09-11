import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static java.util.Arrays.asList;

public class ShadokAddition {

    HashMap<String, Map<String, String>> map = new HashMap<>();

    public ShadokAddition() {
        HashMap<String, String> GAMap = new HashMap<>();
        map.put("GA", GAMap);
        GAMap.put("GA", "GA");
        GAMap.put("BU", "BU");
        GAMap.put("ZO", "ZO");
        GAMap.put("MEU", "MEU");

        HashMap<String, String> BUMap = new HashMap<>();
        map.put("BU", BUMap);
        BUMap.put("GA", "BU");
        BUMap.put("BU", "ZO");
        BUMap.put("ZO", "MEU");
        BUMap.put("MEU", "BU GA");

        HashMap<String, String> ZOMap = new HashMap<>();
        map.put("ZO", ZOMap);
        ZOMap.put("GA", "ZO");
        ZOMap.put("BU", "MEU");
        ZOMap.put("ZO", "BU GA");
        ZOMap.put("MEU", "BU BU");

        HashMap<String, String> MEUMap = new HashMap<>();
        map.put("MEU", MEUMap);
        MEUMap.put("GA", "MEU");
        MEUMap.put("BU", "BU GA");
        MEUMap.put("ZO", "BU BU");
        MEUMap.put("MEU", "BU ZO");
    }

    public String add(String left, String right) {
        List<String> lefts = asList(left.split(" "));
        ListIterator<String> leftIter = lefts.listIterator(lefts.size());
        List<String> rights = asList(right.split(" "));
        ListIterator<String> rightIter = rights.listIterator(rights.size());

        boolean withCarry = false;
        boolean addBu = false;
        StringBuilder result = new StringBuilder();
        while (leftIter.hasPrevious() && rightIter.hasPrevious()) {
            String l = leftIter.previous();
            String r = rightIter.previous();
            if(withCarry) {
                addBu=true;
                withCarry = false;
            }
            String unitResult = addUnits(l, r);
            if (unitResult.startsWith("BU ")) {
                withCarry = true;
                unitResult = unitResult.substring("BU ".length());
            }
            if (addBu) {
                unitResult = addUnits(unitResult, "BU");
                if (unitResult.startsWith("BU ")) {
                    withCarry = true;
                    unitResult = unitResult.substring("BU ".length());
                }
                addBu=false;
            }

            result.insert(0, unitResult + " ");
        }

        while (leftIter.hasPrevious()) {
            String l = leftIter.previous();
            if (withCarry) {
                l = addUnits(l, "BU");
                withCarry=false;
            }
            result.insert(0, l + " ");
        }

        while (rightIter.hasPrevious()) {
            String r = rightIter.previous();
            if (withCarry) {
                r = addUnits(r, "BU");
                withCarry=false;
            }
            result.insert(0, r + " ");
        }

        if(withCarry) {
            result.insert(0, "BU ");
        }
        return result.toString().trim();
    }

    private String addUnits(String left, String right) {
        System.out.println("UNITS : left = " + left + ", right = " + right);
        return map.get(left).get(right);
    }
}
