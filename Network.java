import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.*;

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
                game.gameState = Game.State.WAITING;
            }
        });
        socket.on("player_info", new Emitter.Listener(){
            @Override
            public void call(Object... arg) {
                lock.writeLock().lock();
                try {
                    game.gameState = Game.State.PLAYING;
                    JSONObject info = (JSONObject) arg[0];
                    Player player = new Player(info.getInt("x"), info.getInt("y"), info.getInt("dir"), info.getInt("health"));
                    game.setPlayer(player, info.getInt("id"));
                    playerId = info.getInt("id");
                    SoundManager.playSound("flare", 1.0);
                }catch(Exception e){e.printStackTrace();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("player_updates", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                JSONObject update = (JSONObject)(arg0[0]);
                try {
                    String type = update.getString("type");
                    if(type.equals("new_bandage")) {
                        game.getThisPlayer().setBandages(update.getInt("nums"));
                    } else if(type.equals("new_medkit")){
                        game.getThisPlayer().setMedkits(update.getInt("nums"));
                    } else if(type.equals("helmet")) {
                        game.getThisPlayer().setHelmet(update.getInt("level"));
                    } else if(type.equals("chestplate")) {
                        game.getThisPlayer().setChestplate(update.getInt("level"));
                    } else if(type.equals("update_health")) {
                        game.getThisPlayer().setHealth(update.getInt("health"));
                        game.getThisPlayer().setTotalHealth(update.getInt("totalHealth"));
                    } else if(type.equals("blueAmmo")) {
                        game.getThisPlayer().setBlueAmmo(update.getInt("num"));
                    } else if(type.equals("redAmmo")) {
                        game.getThisPlayer().setRedAmmo(update.getInt("num"));
                    } else if(type.equals("scope")) {
                        game.getThisPlayer().setScope(update.getInt("level"));
                    }
                } catch(Exception e) {e.printStackTrace();}
                finally {
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
                        if(type.equals("player")) {
                            Player updatedPlayer = game.getPlayers().get(update.getInt("id"));
                            if(updatedPlayer == null) 
                                continue;
                            updatedPlayer.setX(update.getInt("x"));
                            updatedPlayer.setY(update.getInt("y"));
                            updatedPlayer.setDirection(update.getInt("dir"));
                            updatedPlayer.setHealth(update.getInt("health"));
                            updatedPlayer.setTotalHealth(update.getInt("totalHealth"));
                            updatedPlayer.setHelmet(update.getInt("helmet"));
                            updatedPlayer.setChestplate(update.getInt("chestplate"));
                            updatedPlayer.setBandages(update.getInt("bandages"));
                            updatedPlayer.setMedkits(update.getInt("medkits"));
                        } else if(type.equals("punch")) {
                            Player updatedPlayer = game.getPlayers().get(update.getInt("id"));
                            updatedPlayer.punch();
                        } else if(type.equals("new_bullet")) {
                            Bullet newBullet = new Bullet(update.getInt("x"), update.getInt("y"), update.getInt("dir"));
                            newBullet.setThickness(update.getInt("thickness"));
                            game.getBullets().put(update.getInt("id"), newBullet);

                            double distance = Math.sqrt(Math.pow(newBullet.getBackX() - game.getPlayer().getX(), 2) + Math.pow(newBullet.getBackY() - game.getPlayer().getY(), 2));
                            if(distance < 5000) {
                                SoundManager.playSound("gun", Math.max(1, 100.0 / distance));
                            }
                        } else if(type.equals("bullet")) {
                            Bullet updatedBullet = game.getBullets().get(update.getInt("id"));
                            updatedBullet.setX(update.getInt("x"));
                            updatedBullet.setY(update.getInt("y"));
                        } else if(type.equals("equip")) {
                            Player equipPlayer = game.getPlayers().get(update.getInt("id"));
                            equipPlayer.setEquippedIndex(update.getInt("index"));
                        } else if(type.equals("remove_bullet")) {
                            game.getBullets().remove(update.getInt("id"));
                        } else if(type.equals("obstacle")) {
                            game.getObstacles().get(update.getInt("id")).setHealth(update.getInt("h"));
                        } else if(type.equals("item")) {
                            Item item = game.getItems().get(update.getInt("id"));
                            item.setX(update.getInt("x"));
                            item.setY(update.getInt("y"));
                        } else if(type.equals("remove_item")) {
                            game.getItems().remove(update.getInt("id"));
                        } else if(type.equals("pickup_weapon")) {
                            Player player = game.getPlayers().get(update.getInt("id"));
                            Gun gun;
                            if(update.getString("gt").equals("rifle")) {
                                gun = new Rifle(player);
                            } else if(update.getString("gt").equals("shotgun")) {
                                gun = new Shotgun(player);
                            } else if(update.getString("gt").equals("sniper")) {
                                gun = new Sniper(player);
                            } else if(update.getString("gt").equals("pistol")) {
                                gun = new Pistol(player);
                            } else {
                                throw new RuntimeException("Unknown gun type! " + update.getString("gt"));
                            }
                            gun.setClip(0);
                            gun.setSpare(update.getInt("spare"));
                            player.getGuns().put(update.getInt("index"), gun);
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
        socket.on("ammo", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                JSONObject update = (JSONObject)(arg0[0]);
                try {
                    game.getPlayer().updateAmmo(update.getInt("equip"), update.getInt("clip"), update.getInt("blue"), update.getInt("red"));
                } catch(Exception e){e.printStackTrace();}
                finally {
                    lock.writeLock().unlock();
                }
            }
        });

        socket.on("new_obstacle", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                JSONObject update = (JSONObject)(arg0[0]);
                try {
                    String type = update.getString("type");
                    if(type.equals("rock")) {
                        game.getObstacles().put(update.getInt("id"), new Stone(update.getInt("x"), update.getInt("y")));
                    } else if(type.equals("bush")) {
                        game.getObstacles().put(update.getInt("id"), new Bush(update.getInt("x"), update.getInt("y")));
                    } else if(type.equals("tree")) {
                        game.getObstacles().put(update.getInt("id"), new Tree(update.getInt("x"), update.getInt("y")));
                    } else if(type.equals("box")) {
                        game.getObstacles().put(update.getInt("id"), new Box(update.getInt("x"), update.getInt("y")));
                    } else if(type.equals("barrel")) {
                        Barrel b = new Barrel(update.getInt("x"), update.getInt("y"));
                        game.getObstacles().put(update.getInt("id"), b);
                    } else {
                        throw new RuntimeException("Invalid obstacle type: " + type);
                    }
                    game.getObstacles().get(update.getInt("id")).setHealth(update.getInt("h"));
                } catch(Exception e){e.printStackTrace();}
                finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("new_dropped_item", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                JSONObject update = (JSONObject)(arg0[0]);
                try {
                    String type = update.getString("type");
                    if(type.equals("rifle")) {
                        game.getItems().put(update.getInt("id"), new DroppedRifle(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("shotgun")) {
                        game.getItems().put(update.getInt("id"), new DroppedShotgun(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("redAmmo")) {
                        game.getItems().put(update.getInt("id"), new Ammo(update.getInt("x"), update.getInt("y"), Color.RED));
                    }
                    else if(type.equals("blueAmmo")) {
                        game.getItems().put(update.getInt("id"), new Ammo(update.getInt("x"), update.getInt("y"), Color.BLUE));
                    }
                    else if(type.equals("pistol")) {
                        game.getItems().put(update.getInt("id"), new DroppedPistol(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("sniper")) {
                        game.getItems().put(update.getInt("id"), new DroppedSniper(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("bandage")) {
                        game.getItems().put(update.getInt("id"), new Bandage(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("medkit")) {
                        game.getItems().put(update.getInt("id"), new Medkit(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("helmet1")) {
                        game.getItems().put(update.getInt("id"), new HelmetOne(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("helmet2")) {
                        game.getItems().put(update.getInt("id"), new HelmetTwo(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("helmet3")) {
                        game.getItems().put(update.getInt("id"), new HelmetThree(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("chestplate1")) {
                        game.getItems().put(update.getInt("id"), new ChestPlateOne(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("chestplate2")) {
                        game.getItems().put(update.getInt("id"), new ChestPlateTwo(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("chestplate3")) {
                        game.getItems().put(update.getInt("id"), new ChestPlateThree(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("scope2")) {
                        game.getItems().put(update.getInt("id"), new Scope2(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("scope4")) {
                        game.getItems().put(update.getInt("id"), new Scope4(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("scope8")) {
                        game.getItems().put(update.getInt("id"), new Scope8(update.getInt("x"), update.getInt("y")));
                    }
                    else if(type.equals("scope15")) {
                        game.getItems().put(update.getInt("id"), new Scope15(update.getInt("x"), update.getInt("y")));
                    }
                    else {
                        throw new RuntimeException("Invalid dropped item type: " + type);
                    }
                } catch(Exception e) {e.printStackTrace();}
                finally {
                    lock.writeLock().unlock();
                } 

            }
        });
        socket.on("delete_player", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                lock.writeLock().lock();
                try {
                    int id = (int)arg0[0];
                    game.getPlayers().remove(id); 
                    if(id == playerId) {
                        socket.disconnect();
                        SoundManager.shutdown();
                        game.gameState = Game.State.DEAD;
                    }               
                }catch(Exception e){e.printStackTrace();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        socket.on("reload", new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                int time = (int)arg0[0];
                game.getPlayer().setReloading(time);
                if(time == 0) return;
                if(time < 1000) {
                    SoundManager.playSound("reload_01", 1.0);
                } else {
                    SoundManager.playSound("reload_02", 1.0);
                }
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                game.gameState = Game.State.CONNECT_FAILURE;
            }
        });
        socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... arg0) {
                game.gameState = Game.State.CONNECT_FAILURE;
            }
        });
        socket.on("PONG", new Emitter.Listener(){
        
            @Override
            public void call(Object... arg0) {
                game.setPing((System.currentTimeMillis() - (long)arg0[0]));
            }
        });
        socket.connect();
    }

    public void wPressed() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("w_pressed", nil);
    }

    public void aPressed() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("a_pressed", nil);
    }

    public void sPressed() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("s_pressed", nil);
    }

    public void dPressed() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("d_pressed", nil);
    }

    public void wReleased() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("w_released", nil);
    }

    public void aReleased() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("a_released", nil);
    }

    public void sReleased() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("s_released", nil);
    }

    public void dReleased() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("d_released", nil);
    }

    public void rPressed() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("r", nil);
    }

    public void fPressed() {
        socket.emit("f", nil);
    }

    public void click() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("click", nil);
    }

    public void num1() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("1", nil);
    }

    public void num2() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("2", nil);
    }

    public void num3() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("3", nil);
    }

    public void useBandages() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("useBandages", nil);
    }

    public void useMedkit() {
        if(game.gameState == Game.State.PLAYING)
            socket.emit("useMedkits", nil);
    }

    public void mouseLocation(int x, int y) {
        if(game.gameState != Game.State.PLAYING)
            return;
        JSONObject coordinates = new JSONObject();
        coordinates.put("x", x);
        coordinates.put("y", y);
        socket.emit("mouse_loc", coordinates);
    }

    public void ping() {
        socket.emit("PING", System.currentTimeMillis());
    }
}