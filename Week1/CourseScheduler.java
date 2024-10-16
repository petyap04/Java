public class CourseScheduler {

    public static int maxNonOverlappingCourses(int index, int[][] arr){
        if(index>=arr.length){
            return 0;
        }
        int currEndHour = arr[index][1];
        int currCount = 0;
        for(int i = index + 1;i < arr.length;i++){
            if(currEndHour <= arr[i][0]){
                currCount = Math.max(currCount, maxNonOverlappingCourses(i, arr));
            }
        }
        return currCount + 1;
    }

    public static int maxNonOverlappingCourses(int[][] courses){
        for(int i = 0;i < courses.length-1;i++) {
            int min = courses[i][1];
            int currEl = i;
            for (int j = i + 1; j < courses.length; j++) {
                if (min > courses[j][1]) {
                    currEl = j;
                    min = courses[j][1];
                }
            }
            if (courses[i][1] != courses[currEl][1]) {
                int temp1 = courses[i][0];
                int temp2 = courses[i][1];
                courses[i][0] = courses[currEl][0];
                courses[i][1] = courses[currEl][1];
                courses[currEl][0] = temp1;
                courses[currEl][1] = temp2;
            }
        }
        return maxNonOverlappingCourses(0,courses);
    }
    public static void main(String[] args) {
      int[][]courses = {{0, 1}, {2, 4}, {2, 3}, {3, 4},{4, 5}};

      System.out.println(maxNonOverlappingCourses(courses));
  }
}
