package features.inputoutput;

import java.io.IOException;

public interface IGameInput {
    int ARROW_UP = 1000;
    int ARROW_DOWN = 1001;
    int ARROW_RIGHT = 1002;
    int ARROW_LEFT = 1003;

    int readKey() throws IOException;
}
