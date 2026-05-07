package com.example.ecommerce.util;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {

    public static void applyHoverScale(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    public static void fadeIn(Node node, double delayMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.play();
    }
}
