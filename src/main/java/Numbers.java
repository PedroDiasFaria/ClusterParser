import java.util.Arrays;

public class Numbers {

    public static void main(String[] args)
    {
        int t1 = 12;
        int t2 = 513;
        int t3 = 2017;

        int f1 = 9;
        int f2 = 111;
        int f3 = 531;


        long b = 21581957621L;
        Numbers nr = new Numbers();

        System.out.println("nextBigger(" + t1 + ")==" + nr.nextBigger(t1));
        System.out.println("nextBigger(" + t2 + ")==" + nr.nextBigger(t2));
        System.out.println("nextBigger(" + t3 + ")==" + nr.nextBigger(t3));
        System.out.println("nextBigger(" + f1 + ")==" + nr.nextBigger(f1));
        System.out.println("nextBigger(" + f2 + ")==" + nr.nextBigger(f2));
        System.out.println("nextBigger(" + f3 + ")==" + nr.nextBigger(f3));

        System.out.println("nextBigger(" + b + ")==" + nr.nextBigger(b));
    }

    private void swapDigits(int[] array, int a, int b){
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    private long arrayToInt(int[] array){
        StringBuilder strNum = new StringBuilder();

        for (int digit : array)
        {
            strNum.append(digit);
        }
        long finalInt = Long.parseLong(strNum.toString());
        return finalInt;
    }

    public long nextBigger(long number) {


        String numberString = Long.toString(number);
        int nrDigits = numberString.length();
        int[] numberArray = new int[numberString.length()];

        for(int i = 0; i < numberString.length(); i++){
            numberArray[i] = Character.getNumericValue(numberString.charAt(i));
        }

        int charIndex;

        for (charIndex = nrDigits-1; charIndex > 0; charIndex--)
        {
            if (numberArray[charIndex] > numberArray[charIndex-1]) {

                int digitToSwap = numberArray[charIndex-1];
                int swapIndex = charIndex;

                for (int j = charIndex + 1; j < nrDigits; j++)
                {
                    if (numberArray[j] > digitToSwap && numberArray[j] < numberArray[swapIndex])
                    {
                        swapIndex = j;
                    }
                }

                swapDigits(numberArray, charIndex-1, swapIndex);
                Arrays.sort(numberArray, charIndex, nrDigits);

                long nextBigger = arrayToInt(numberArray);

                return nextBigger;
            }
        }

        return -1;

    }
}
