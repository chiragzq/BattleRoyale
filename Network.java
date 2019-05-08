import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Network {
    private Socket socket;
    private Game game;

    private int playerId;

    private Object nil = new Object();

    public Network(String server, Game game) {
        this.game = game;
        try {
            initializeSocket(server);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeSocket(String server) throws Exception {
        socket = IO.socket(server);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to server");
                // Display game is in progress... please wait
            }
        });
        socket.on("player_info", new Emitter.Listener(){
            @Override
            public void call(Object... arg) {
                JSONObject info = (JSONObject) arg[0];
                Player player = new Player(info.getInt("x"), info.getInt("y"), info.getInt("dir"), info.getInt("health"));
                game.setPlayer(player, info.getInt("id"));
                playerId = info.getInt("id");
            }
        });
        socket.on("new_player", new Emitter.Listener(){
            @Override
            public void call(Object... arg) {
                JSONObject info = (JSONObject) arg[0];
                System.out.println(info.getInt("id") + " " + playerId);
                if(info.getInt("id") == playerId) {
                    return;
                }
                Player player = new Player(info.getInt("x"), info.getInt("y"), info.getInt("dir"), info.getInt("health"));
                game.getPlayers().put(info.getInt("id"), player);
            }
        });
        socket.on("update", new Emitter.Listener(){
            @Override
            public void call(Object... objsa) {
                JSONArray objs = (JSONArray)objsa[0];
                JSONObject[] updates = new JSONObject[objs.length()];
                for(int i = 0;i < objs.length();i ++) {
                    updates[i] = (JSONObject) objs.getJSONObject(i);
                }
                for(JSONObject update : updates) {
                    if(update.getString("type").equals("player")) {
                        Player updatedPlayer = game.getPlayers().get(update.getInt("id"));
                        updatedPlayer.setX(update.getInt("x"));
                        updatedPlayer.setY(update.getInt("y"));
                        updatedPlayer.setDirection(update.getInt("dir"));
                        updatedPlayer.setHealth(update.getInt("health"));
                    } else {
                        throw new RuntimeException("Unknown Update Type! " + update.getString("type"));
                    }
                }
            }
        });
        socket.on("delete_player", new Emitter.Listener(){
            @Override
            public void call(Object... arg0) {
                int id = (int)arg0[0];
                game.getPlayers().remove(id);
            }
        });
        socket.connect();
    }

    public void wPressed() {
        socket.emit("w_pressed", nil);
    }

    public void aPressed() {
        socket.emit("a_pressed", nil);
    }

    public void sPressed() {
        socket.emit("s_pressed", nil);
    }

    public void dPressed() {
        socket.emit("d_pressed", nil);
    }

    public void wReleased() {
        socket.emit("w_released", nil);
    }

    public void aReleased() {
        socket.emit("a_released", nil);
    }

    public void sReleased() {
        socket.emit("s_released", nil);
    }

    public void dReleased() {
        socket.emit("d_released", nil);
    }
}