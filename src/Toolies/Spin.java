package Toolies;

import java.util.ArrayList;

public class Spin {
    private static int[][] rotate(int[][] unrotated) {

        int[][] rotated = new int[unrotated.length][unrotated.length];
        int index = unrotated.length-1;
        for(int i = unrotated.length; i > 0; i--) {
            for(int j = 0; j < unrotated.length; j++) {
                rotated[i-1][j] = unrotated[index][i-1];
                index--;
                if(index == -1) {
                    index = unrotated.length-1;
                }
            }


        }


        return rotated;
    }

    public static ArrayList<Integer> rotate(ArrayList<Integer> toRotate) {
        int length = 0;
        ArrayList<Integer> result = null;
        try {
            length = (int) Math.sqrt(toRotate.size());
            result = new ArrayList<>();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int[][] rotated = new int[length][length];

        int index = (length * length) - 1;

        for(int i = length; i > 0; i--) {
            for(int j = 0; j < length; j++) {
                rotated[i-1][j] = toRotate.get(index);
                index -= 3;
                if(index < 0) {
                    index += (length * length)-1;
                }

            }

        }

        for(int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                assert result != null;
                result.add(rotated[i][j]);
            }
        }


        return result;
    }
}
