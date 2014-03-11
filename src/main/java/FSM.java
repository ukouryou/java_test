/**
 * Feb 14, 2014
 * @author andy
 */
public class FSM {

    static State[][] transTable=new State[State.values().length][10];

    static{
        transTable[State.start.ordinal()][1]=State.chinese;
        transTable[State.start.ordinal()][2]=State.english;
        transTable[State.chinese.ordinal()][0]=State.end;
        transTable[State.english.ordinal()][0]=State.end;
    }

    State current=State.start;

    State step(State s,char c){
        current=transTable[s.ordinal()][c-'0'];
        return current;
    }

    public static void main(String[] args) {
        FSM fsm=new FSM();
        System.out.println(fsm.step(fsm.current,'1'));
        System.out.println(fsm.step(fsm.current,'0'));
    }

}

enum State {
    start,
    chinese,
    english,
    end
}