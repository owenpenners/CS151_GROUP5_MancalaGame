public class MancalaTest {
    public static void main(String[] args){
        MancalaModel model = new MancalaModel();
        MancalaView view = new MancalaView();
        MancalaController controller = new MancalaController(model, view);

    }
}
