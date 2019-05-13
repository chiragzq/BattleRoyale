import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Network {
    private Socket socket;
    private Game game;

    private int playerId;
    
    private ReadWriteLock lock;

    private Object nil = new Object();

    public Network(String server, Game game, ReadWriteLock lock) {
        this.game = game;
        this.lock = lock;
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
                lock.writeLock().lock();
                try {
                    JSONObject info = (JSONObject) arg[0];
                    Player player = new Player(info.getInt("x"), info.getInt("y"), info.getInt("dir"), info.getInt("health"));
                    game.setPlayer(player, info.getInt("id"));
                    playerId = info.getInt("id");                }catch(Exception e){e.printStackTrace();

                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("new_player", new Emitter.Listener(){
            @Override
            public void call(Object... arg) {
                lock.writeLock().lock();
                try {
                    JSONObject info = (JSONObject) arg[0];
                    System.out.println(info.getInt("id") + " " + playerId);
                    if(info.getInt("id") == playerId) {
                        return;
                    }
                    Player player = new Player(info.getInt("x"), info.getInt("y"), info.getInt("dir"), info.getInt("health"));
                    //player.setEquippedIndex(info.getInt("equip"));
                    game.getPlayers().put(info.getInt("id"), player);               }catch(Exception e){e.printStackTrace();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("update", new Emitter.Listener(){
            @Override
            public void call(Object... objsa) {
                lock.writeLock().lock();
                try {
                    JSONArray objs = (JSONArray)objsa[0];
                    JSONObject[] updates = new JSONObject[objs.length()];
                    for(int i = 0;i < objs.length();i ++) {
                        updates[i] = (JSONObject) objs.getJSONObject(i);
                    }
                    for(JSONObject update : updates) {
                        String type = update.getString("type");
                        if(type.equals("ping")) {
                            game.setPing((int)(System.currentTimeMillis() - update.getLong("t")));
                        } else if(type.equals("player")) {
                            Player updatedPlayer = game.getPlayers().get(update.getInt("id"));
                            updatedPlayer.setX(update.getInt("x"));
                            updatedPlayer.setY(update.getInt("y"));
                            updatedPlayer.setDirection(update.getInt("dir"));
                            updatedPlayer.setHealth(update.getInt("health"));
                        } else if(type.equals("punch")) {
                            Player updatedPlayer = game.getPlayers().get(update.getInt("id"));
                            updatedPlayer.punch();
                        } else if(type.equals("new_bullet")) {
                            Bullet newBullet = new Bullet(update.getInt("x"), update.getInt("y"), update.getInt("dir"));
                            game.getBullets().put(update.getInt("id"), newBullet);
                        } else if(type.equals("bullet")) {
                            Bullet updatedBullet = game.getBullets().get(update.getInt("id"));
                            updatedBullet.setX(update.getInt("x"));
                            updatedBullet.setY(update.getInt("y"));
                        } else if(type.equals("equip")) {
                            Player equipPlayer = game.getPlayers().get(update.getInt("id"));
                            equipPlayer.setEquippedIndex(update.getInt("index"));
                        } else if(type.equals("remove_bullet")) {
                            game.getBullets().remove(update.get("id"));
                        } else {
                            throw new RuntimeException("Unknown Update Type! " + type);
                        }
                    }
                }catch(Exception e){e.printStackTrace();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("delete_player", new Emitter.Listener(){
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                try {
                    int id = (int)arg0[0];
                    game.getPlayers().remove(id);                }catch(Exception e){e.printStackTrace();

                } finally {
                    lock.writeLock().unlock();
                }
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

    public void click() {
        socket.emit("click", nil);
    }

    public void num1() {
        socket.emit("1", nil);
    }

    public void num2() {
        socket.emit("2", nil);
    }

    public void num3() {
        socket.emit("3", nil);
    }
 
    public void mouseLocation(int x, int y) {
        JSONObject coordinates = new JSONObject();
        coordinates.put("x", x);
        coordinates.put("y", y);
        socket.emit("mouse_loc", coordinates);
    }
}