import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

public class ShadokAddition {

    public static final String CARRY = "BU";
    final HashMap<String, Map<String, String>> map = new HashMap<>();

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

    boolean withCarry = false;
    boolean addBu = false;

    public String add(String left, String right) {
        ListIterator<String> leftIter = new InfiniteLeftPadder(left);
        ListIterator<String> rightIter = new InfiniteLeftPadder(right);
        withCarry = false;
        addBu = false;

        StringBuilder result = new StringBuilder();
        while (leftIter.hasPrevious() || rightIter.hasPrevious()) {
            String l = leftIter.previous();
            String r = rightIter.previous();

            findCarryOnUnitsAddsBUOnTens();

            String unitResult = addUnitsHandlingCarry(l, r);
            if (addBu) {
                unitResult = addUnitsHandlingCarry(unitResult, CARRY);
                addBu = false;
            }

            result.insert(0, unitResult + " ");
        }

        if (withCarry) {
            result.insert(0, "BU ");
        }
        return result.toString().trim();
    }

    private String addUnitsHandlingCarry(String l, String r) {
        String unitResult = addUnits(l, r);
        if (unitResult.startsWith("BU ")) {
            withCarry = true;
            unitResult = unitResult.substring("BU ".length());
        }
        return unitResult;
    }

    private void findCarryOnUnitsAddsBUOnTens() {
        if (withCarry) {
            addBu = true;
            withCarry = false;
        }
    }

    private String addUnits(String left, String right) {
        System.out.println("UNITS : left = " + left + ", right = " + right);
        return map.get(left).get(right);
    }

    private class InfiniteLeftPadder implements ListIterator<String> {

        private ListIterator<String> digitsIterator;

        public InfiniteLeftPadder(String number) {
            List<String> digits = asList(number.split(" "));
            this.digitsIterator = digits.listIterator(digits.size());
        }

        @Override
        public boolean hasPrevious() {
            return digitsIterator.hasPrevious();
        }

        @Override
        public String previous() {
            if (digitsIterator.hasPrevious()) {
                return digitsIterator.previous();
            } else {
                return "GA";
            }
        }

        //// delegation boiler plate under this line
        @Override
        public boolean hasNext() {
            return digitsIterator.hasNext();
        }

        @Override
        public String next() {
            return digitsIterator.next();
        }

        @Override
        public int nextIndex() {
            return digitsIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return digitsIterator.previousIndex();
        }

        @Override
        public void remove() {
            digitsIterator.remove();
        }

        @Override
        public void set(String s) {
            digitsIterator.set(s);
        }

        @Override
        public void add(String s) {
            digitsIterator.add(s);
        }

        @Override
        public void forEachRemaining(Consumer<? super String> action) {
            digitsIterator.forEachRemaining(action);
        }
    }
}
