import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class App {

    /*4 задание реализовано частично - блокируются ходы по вертикали/горизонтали, 
    * Кроме того, возникает коллизия на более позних этапах игры, 
    * когда существуют заблокированные варианты с большим числом крестиков, чем с открытыми. 
    * Начал кэшировать эти варианты, но е успел довести до ума 
    * По диагонали в принципе решается все аналогично
    */
    
    public static final int SIZE = 5;

    public static final int DOTS_TO_WIN = 4;

    public static final char DOT_EMPTY = '*';

    public static final char DOT_X = 'x';

    public static final char DOT_O = 'o';

    public static int maxWin;

    public static char[][] map;

    static MaxCombination maxComb = new App.MaxCombination(SIZE, SIZE, SIZE, SIZE, SIZE);
    
    static Set<MaxCombination> maxCombTested = new HashSet<>();

    public static Scanner scanner = new Scanner(System.in);

    public static Random random = new Random();

    public static void main(String[] args) throws Exception {

        initMap();
        ptintMap();

        while (true) {

            humanTurn();
            ptintMap();

            if (checkWin(DOT_X, DOTS_TO_WIN, false)) {

                System.out.println("Вы победили!");
                break;

            }

            

            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }



            aiTurn();
            ptintMap();

            if (checkWin(DOT_O, DOTS_TO_WIN, false)) {

                System.out.println("Победил компьютер!");
                break;

            }

           

            if (isMapFull()) {
                System.out.println("Ничья");
                break;
            }

        }

        System.out.println("Игра окончена");

    }

    public static void initMap() {

        map = new char[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {

            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }

        }

    }

    public static void ptintMap() {

        String xAxis = "  ";

        for (int i = 0; i < SIZE; i++) {

            xAxis += i + " ";

        }

        System.out.print(xAxis);

        System.out.println("");

        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < map.length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println("");
        }

    }

    public static boolean checkWin(char symb, final int dotsToWin, boolean isBot) {
        final int diffForWin = SIZE - dotsToWin + 1;
        maxWin = 0;
        int oldMaxWin = 0;
        

        // проверка выигрыша по горизонтали
        for (int i = 0; i < map.length; i++) {
            int accumXWin = 0;
            int accumXNotWin = 0;
            for (int j = 0; j < map.length; j++) {

                if (map[i][j] == symb) {
                    accumXWin++;
                    maxWin = Math.max(maxWin, accumXWin);
                    // кэшируем проверенные комбинации по горизонтали
                    MaxCombination tmpMaxComb = new App.MaxCombination(maxWin, j - maxWin + 1, i, j, i);
                    if (isBot && maxWin > oldMaxWin && !maxCombTested.contains(tmpMaxComb)) 
                    {
                        oldMaxWin = maxWin;
                        maxCombTested.add(tmpMaxComb);
                        maxComb.setX1(j - maxWin + 1);
                        maxComb.setY1(i);
                        maxComb.setX2(j);
                        maxComb.setY2(i);
                        maxComb.setMaxWin(maxWin);
 
                    }
                    

                    if (accumXWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[i][j] != symb) {
                    accumXNotWin++;
                    if (accumXNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumXWin = 0;
                    }
                }

            }
        }

        // проверка выигрыша по вертикали
        for (int i = 0; i < map.length; i++) {
            int accumYWin = 0;
            int accumYNotWin = 0;
            for (int j = 0; j < map.length; j++) {

                if (map[j][i] == symb) {
                    accumYWin++;
                    maxWin = Math.max(maxWin, accumYWin);
                    
                    // кэшируем проверенные комбинации по вертикали
                    MaxCombination tmpMaxComb = new App.MaxCombination(maxWin, i, j - maxWin + 1, i, j);
                    if (isBot && maxWin > oldMaxWin && !maxCombTested.contains(tmpMaxComb)) {
                        maxCombTested.add(tmpMaxComb);
                        oldMaxWin = maxWin;
                        maxComb.setX1(i);
                        maxComb.setY1(j - maxWin + 1);
                        maxComb.setX2(i);
                        maxComb.setY2(j);
                        maxComb.setMaxWin(maxWin);


                    }
                    if (accumYWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[j][i] != symb) {
                    accumYNotWin++;
                    if (accumYNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumYWin = 0;
                    }
                }

            }
        }

        // проверка выигрыша по диагонали лево-верх/право-низ
        for (int diff = 0; diff < diffForWin; diff++) {
            // проверка выше диагонали
            int accumDiagWin = 0;
            int accumDiagNotWin = 0;
            for (int i = 0; i < map.length - diff; i++) {

                if (map[i][i + diff] == symb) {
                    accumDiagWin++;
                    maxWin = Math.max(maxWin, accumDiagWin);
                    if (accumDiagWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[i][i + diff] != symb) {
                    accumDiagNotWin++;
                    if (accumDiagNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumDiagWin = 0;
                    }
                }

            }

            accumDiagWin = 0;
            accumDiagNotWin = 0;
            // проверка ниже диагонали
            for (int i = 0; i < map.length - diff; i++) {

                if (map[i + diff][i] == symb) {
                    accumDiagWin++;
                    maxWin = Math.max(maxWin, accumDiagWin);
                    if (accumDiagWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[i + diff][i] != symb) {
                    accumDiagNotWin++;
                    if (accumDiagNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumDiagWin = 0;
                    }
                }

            }
        }

        // проверка выигрыша по диагонали лево-низ/право-верх
        for (int diff = 0; diff < diffForWin; diff++) {
            // проверка выше диагонали
            int accumDiagWin = 0;
            int accumDiagNotWin = 0;

            for (int i = 0; i < map.length - diff; i++) {

                if (map[map.length - diff - i - 1][i] == symb) {
                    accumDiagWin++;
                    maxWin = Math.max(maxWin, accumDiagWin);
                    if (accumDiagWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[map.length - diff - i - 1][i] != symb) {
                    accumDiagNotWin++;
                    if (accumDiagNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumDiagWin = 0;
                    }
                }

            }
            accumDiagWin = 0;
            accumDiagNotWin = 0;
            // проверка ниже диагонали
            for (int i = 0; i < map.length - diff; i++) {

                if (map[map.length - i - 1][i + diff] == symb) {
                    accumDiagWin++;
                    maxWin = Math.max(maxWin, accumDiagWin);
                    if (accumDiagWin == DOTS_TO_WIN) {
                        return true;
                    }
                    continue;
                }

                if (map[map.length - i - 1][i + diff] != symb) {
                    accumDiagNotWin++;
                    if (accumDiagNotWin > diffForWin - 1) {
                        break;
                    } else {
                        accumDiagWin = 0;
                    }
                }

            }
        }
        
       

        return false;
    }

    public static boolean isMapFull() {

        for (int i = 0; i < map.length; i++) {

            for (int j = 0; j < map.length; j++) {

                if (map[i][j] == DOT_EMPTY) {

                    return false;

                }

            }

        }

        return true;

    }

    public static boolean isCellValid(int x, int y) {

        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return false;
        }

        return map[y][x] == DOT_EMPTY;

    }

    public static void humanTurn() {

        int x, y;

        do {

            System.out.println("Введите координаты в формате x y");

            x = scanner.nextInt();
            y = scanner.nextInt();

        }

        while (!isCellValid(x, y));
        map[y][x] = DOT_X;
    }

    public static void aiTurn() {

        int x=0, y=0;

        /*
         * 4 задание реализовано частично - блокируются ходы по вертикали/горизонтали,
         * Кроме того, возникает коллизия на более позних этапах игры, когда существуют
         * заблокированные варианты с большим числом крестиков, чем с открытыми. Начал
         * кэшировать эти варианты, но е успел довести до ума
         * По диагонали в принципе решается все аналогично
         */
        checkWin(DOT_X, DOTS_TO_WIN-2, true);

        

        int tryer = 0;

        do {
            // сначала блокируем слева или сверху
            if (tryer == 0) {
                if (maxComb.getY1() == maxComb.getY2()) {
                   
                    x = maxComb.getX1()-1;
                    y = maxComb.getY1();
                    tryer++;
                    continue;
                }
                else if (maxComb.getX1() == maxComb.getX2()) {
                   
                    x = maxComb.getX1();
                    y = maxComb.getY1() - 1;
                    
                    tryer++;
                    continue;
                
                }
                
               
            }

            // сначала блокируем справа или снизу
            if (tryer == 1) {
                if (maxComb.getY1() == maxComb.getY2()) {
                    x = maxComb.getX2()+1;
                    y = maxComb.getY2();
                    tryer++;
                    continue;
                }
                else if (maxComb.getX1() == maxComb.getX2()) {
                    x = maxComb.getX2();
                    y = maxComb.getY2() + 1;
                    tryer++;
                    continue;

                }
               
                
            }
           
            // блокировка оказалась не валидной, ход выбираем стандартныс образом
            x = random.nextInt(SIZE);
            y = random.nextInt(SIZE);
           

        }

        while (!isCellValid(x, y));
        map[y][x] = DOT_O;

    }

    static class MaxCombination {
        private int maxWin;
        private int x1;
        private int y1;
        private int x2;
        private int y2;

        

        /**
         * @param maxWin the maxWin to set
         */
        public void setMaxWin(int maxWin) {
            this.maxWin = maxWin;
        }

        /**
         * @param x1 the x1 to set
         */
        public void setX1(int x1) {
            this.x1 = x1;
        }

        /**
         * @param x2 the x2 to set
         */
        public void setX2(int x2) {
            this.x2 = x2;
        }

        /**
         * @param y1 the y1 to set
         */
        public void setY1(int y1) {
            this.y1 = y1;
        }

        /**
         * @param y2 the y2 to set
         */
        public void setY2(int y2) {
            this.y2 = y2;
        }

        @Override
        public String toString() {

            return "x1: " + x1 + "; y1: " + y1 + "; x2: " + x2 + "; y2: " + y2 + "; maxWin: "+ maxWin ;
            
        }

        /**
         * @return the maxWin
         */
        public int getMaxWin() {
            return maxWin;
        }

        /**
         * @return the x1
         */
        public int getX1() {
            return x1;
        }

        /**
         * @return the y1
         */
        public int getY1() {
            return y1;
        }

        /**
         * @return the x2
         */
        public int getX2() {
            return x2;
        }

        /**
         * @return the y2
         */
        public int getY2() {
            return y2;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + maxWin;
            result = prime * result + x1;
            result = prime * result + x2;
            result = prime * result + y1;
            result = prime * result + y2;
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MaxCombination other = (MaxCombination) obj;
            if (maxWin != other.maxWin) {
                return false;
            }
            if (x1 != other.x1) {
                return false;
            }
            if (x2 != other.x2) {
                return false;
            }
            if (y1 != other.y1) {
                return false;
            }
            if (y2 != other.y2) {
                return false;
            }
            return true;
        }

        /**
         * @param maxWin
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         */
        public MaxCombination(int maxWin, int x1, int y1, int x2, int y2) {
            this.maxWin = maxWin;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        

    }
}
