import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class ShadokAddition {
    enum ShadokDigits {
        GA, BU, ZO, MEU;

        AdditionResultDigit add(ShadokDigits other) {
            int sum = this.ordinal() + other.ordinal();
            if (sum >= ShadokDigits.values().length) {
                return new AdditionResultDigit(
                        ShadokDigits.values()[sum - ShadokDigits.values().length],
                        true);
            }
            return new AdditionResultDigit(
                    ShadokDigits.values()[sum],
                    false);
        }
    }

    public static final ShadokDigits CARRY = ShadokDigits.values()[1];

    public String add(String left, String right) {
        ListIterator<ShadokDigits> leftIter = new InfiniteLeftPadder(left);
        ListIterator<ShadokDigits> rightIter = new InfiniteLeftPadder(right);
        ArrayList<AdditionResultDigit> resultList = new ArrayList<>(
                max(
                        left.length(),
                        right.length())
                        + 1);
        while (leftIter.hasPrevious() || rightIter.hasPrevious()) {
            ShadokDigits l = leftIter.previous();
            ShadokDigits r = rightIter.previous();

            AdditionResultDigit additionResultDigit = l.add(r);
            if (!resultList.isEmpty()) {
                if (resultList.get(0).isCarry()) {
                    additionResultDigit = additionResultDigit.addCarry(CARRY);
                }
            }
            resultList.add(0, additionResultDigit);

        }

        if (resultList.get(0).isCarry()) {
            resultList.add(0, new AdditionResultDigit(CARRY, false));
        }
        return resultList.stream().map(item -> item.digit.toString()).collect(joining(" "));
    }

    private static class AdditionResultDigit {
        private final ShadokDigits digit;
        private final boolean carry;

        public AdditionResultDigit(ShadokDigits digit, boolean carry) {
            this.digit = digit;
            this.carry = carry;
        }

        public boolean isCarry() {
            return carry;
        }

        public ShadokDigits getDigit() {
            return digit;
        }

        public AdditionResultDigit addCarry(ShadokDigits carry) {
            AdditionResultDigit result = digit.add(carry);
            return new AdditionResultDigit(result.digit, result.carry || this.carry);
        }
    }

    private class InfiniteLeftPadder implements ListIterator<ShadokDigits> {

        private ListIterator<ShadokDigits> digitsIterator;

        public InfiniteLeftPadder(String number) {
            List<ShadokDigits> collect = stream(
                    number.split(" "))
                    .map(item ->
                            ShadokDigits.valueOf(item))
                    .collect(Collectors.toList());
            this.digitsIterator = collect.listIterator(collect.size());
        }

        @Override
        public boolean hasPrevious() {
            return digitsIterator.hasPrevious();
        }

        @Override
        public ShadokDigits previous() {
            if (digitsIterator.hasPrevious()) {
                return digitsIterator.previous();
            } else {
                return ShadokDigits.GA;
            }
        }

        //// delegation boiler plate under this line
        @Override
        public boolean hasNext() {
            return digitsIterator.hasNext();
        }

        @Override
        public ShadokDigits next() {
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
        public void set(ShadokDigits s) {
            digitsIterator.set(s);
        }

        @Override
        public void add(ShadokDigits s) {
            digitsIterator.add(s);
        }

        @Override
        public void forEachRemaining(Consumer<? super ShadokDigits> action) {
            digitsIterator.forEachRemaining(action);
        }
    }

}
