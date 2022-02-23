//package ca.benbingham.game.gameclasses;
//
//import static ca.benbingham.engine.util.Printing.print;
//
//public class SyncThread extends Thread{
//    private Game game;
//
//    private Renderer rendererThread;
//    private Updater updaterThread;
//
////    private ArrayList<Float> worldVertexData;
////    private ArrayList<Integer> worldIndexData;
////
////    private ArrayList<Float> oldWorldVertexData;
////    private ArrayList<Integer> oldWorldIndexData;
//
//    private boolean gameOpen = true;
//
//    // flags
//    private boolean newWorldData = false;
//    private boolean updateThreadFinished = false;
//    private boolean renderThreadFinished = false;
//    private boolean firstFrameDataCreated = false;
//
//    public SyncThread(Game game) {
//        this.game = game;
//    }
//
//    @Override
//    public void run() {
//        init();
//
//        while (gameOpen) {
//            update();
//        }
//
//        destroy();
//    }
//
//    // Create update and render threads
//    private void init() {
//        updaterThread = new Updater(this);
//        updaterThread.start();
//
//        try { Thread.sleep(5); } catch (Exception ignored) { }
//
//        rendererThread = new Renderer(this);
//        rendererThread.start();
//    }
//
//    // game loop
//    private void update() {
////        // get data from update thread
////        worldVertexData = updaterThread.fetchWorldVertexData();
////        worldIndexData = updaterThread.fetchWorldIndexData();
////
////        // send data to be rendered
////        //if (worldVertexData != oldWorldVertexData || worldIndexData != oldWorldIndexData) {
////
////        if (true) {
////            try {
////                rendererThread.setCount(updaterThread.getCount());
////
////                rendererThread.uploadData(worldVertexData, worldIndexData);
////                oldWorldIndexData = worldIndexData;
////                oldWorldVertexData = worldVertexData;
////            }
////            catch (NullPointerException ignored) { }
////        }
//
//        updaterThread.setStartProcess(true);
//        rendererThread.setStartProcess(true);
//
//        //try { Thread.sleep(16, 66666); } catch(Exception ignored) {}
//
//
//        //updaterThread.notify();
//        //rendererThread.notify();
////        while(!isUpdateThreadFinished()) {
////        }
////
////        while(!isRenderThreadFinished()) {
////        }
//
//    }
//
//    public void destroy() {
//        updaterThread.destroy(); //TODO cant be called from this thread
//        rendererThread.destroy();
//        System.exit(0);
//    }
//
//    public boolean isGameOpen() {
//        return gameOpen;
//    }
//
//    public void setGameOpen(boolean gameOpen) {
//        this.gameOpen = gameOpen;
//    }
//
//    public synchronized Game getGame() {
//        return game;
//    }
//
//    public synchronized void setGame(Game game) {
//        this.game = game;
//    }
//
//    public synchronized void toggleUpdateThreadStatusFlag() {
//        updateThreadFinished = !updateThreadFinished;
//    }
//
//    public synchronized void toggleRenderThreadStatusFlag() {
//        renderThreadFinished = !renderThreadFinished;
//    }
//
//
//    public boolean isNewWorldData() {
//        return newWorldData;
//    }
//
//    public void setNewWorldData(boolean newWorldData) {
//        this.newWorldData = newWorldData;
//    }
//
//    public boolean isUpdateThreadFinished() {
//        return updateThreadFinished;
//    }
//
//    public void setUpdateThreadFinished(boolean updateThreadFinished) {
//        this.updateThreadFinished = updateThreadFinished;
//    }
//
//    public boolean isRenderThreadFinished() {
//        return renderThreadFinished;
//    }
//
//    public void setRenderThreadFinished(boolean renderThreadFinished) {
//        this.renderThreadFinished = renderThreadFinished;
//    }
//
//    public Renderer getRenderThread() {
//        return rendererThread;
//    }
//
//    public Updater getUpdateThread() {
//        return updaterThread;
//    }
//
//    public boolean isFirstFrameDataCreated() {
//        return firstFrameDataCreated;
//    }
//
//    public void setFirstFrameDataCreated(boolean firstFrameDataCreated) {
//        this.firstFrameDataCreated = firstFrameDataCreated;
//    }
//}
