import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

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
        ListIterator<String> leftIter = new InfiniteLeftPadder(lefts.listIterator(lefts.size()));
        List<String> rights = asList(right.split(" "));
        ListIterator<String> rightIter = new InfiniteLeftPadder(rights.listIterator(rights.size()));

        boolean withCarry = false;
        boolean addBu = false;
        StringBuilder result = new StringBuilder();
        while (leftIter.hasPrevious() || rightIter.hasPrevious()) {
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

        if(withCarry) {
            result.insert(0, "BU ");
        }
        return result.toString().trim();
    }

    private String addUnits(String left, String right) {
        System.out.println("UNITS : left = " + left + ", right = " + right);
        return map.get(left).get(right);
    }

    private class InfiniteLeftPadder implements ListIterator<String> {

        private ListIterator<String> stringListIterator;

        public InfiniteLeftPadder(ListIterator<String> stringListIterator) {
            this.stringListIterator = stringListIterator;
        }

        @Override
        public boolean hasPrevious() {
            return stringListIterator.hasPrevious();
        }

        @Override
        public String previous() {
            if (stringListIterator.hasPrevious()) {
                return stringListIterator.previous();
            } else {
                return "GA";
            }
        }

        //// delegation boiler plate under this line
        @Override
        public boolean hasNext() {
            return stringListIterator.hasNext();
        }

        @Override
        public String next() {
            return stringListIterator.next();
        }

        @Override
        public int nextIndex() {
            return stringListIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return stringListIterator.previousIndex();
        }

        @Override
        public void remove() {
            stringListIterator.remove();
        }

        @Override
        public void set(String s) {
            stringListIterator.set(s);
        }

        @Override
        public void add(String s) {
            stringListIterator.add(s);
        }

        @Override
        public void forEachRemaining(Consumer<? super String> action) {
            stringListIterator.forEachRemaining(action);
        }
    }
}
