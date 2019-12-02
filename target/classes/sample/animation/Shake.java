package animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private TranslateTransition shaker;

    public Shake(Node node){
        shaker = new TranslateTransition(Duration.millis(70),node);
        shaker.setFromX(0);
        shaker.setByX(3f);
        shaker.setCycleCount(4);
        shaker.setAutoReverse(true);
    }

    public void playAnimation()
    {
        shaker.playFromStart();
    }
}
