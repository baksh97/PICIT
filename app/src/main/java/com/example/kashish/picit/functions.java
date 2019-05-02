package com.example.kashish.picit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.Vector;

import static com.example.kashish.picit.MainActivity.userFolder;

public class functions {
    private static final String TAG = "functions";
    static Bitmap b;
//    static int count=0;
//    static galleryImages_rv_adapter adapter;
//    static File chatFile;

    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference();


    static boolean isLoggedIn(){
        if(mAuth.getCurrentUser()!=null){
            return true;
        }
        else{
            return false;
        }
    }

    static void signout(){
        mAuth.signOut();
    }

    static Drawable bitmap2Drawable(Bitmap b, Context context){
        Drawable d = new BitmapDrawable(context.getResources(), b);
        return d;
    }

    static void getUpdates(Context context, String chatName, int chatID){
        Vector<String> images = getPicturesInGroup(chatID);
        Vector<Integer> albums = getAlbumsInGroup(chatID);

        Log.d(TAG,"found albums in chat: "+albums.size());

        if(images==null){
            Toast.makeText(context, "Could not load images! Please try again later.", Toast.LENGTH_SHORT).show();
        }else if(albums==null){
            Toast.makeText(context, "Could not load albums! Please try again later.", Toast.LENGTH_SHORT).show();
        } else{
            String chatFolder = chatName+String.valueOf(chatID);

            for(String s: images){
                String[] parts = s.split(",");

                String picid = parts[0];
                String picName = picid+".jpg";

                downloadImaeFromFirebaseStorage(context,userFolder,chatFolder,picName);
            }

            for(int i: albums){
                String albumName = getAlbumNameFromId(i);
                String albumPath = chatFolder+"/"+"album_"+albumName+"\t"+String.valueOf(i);

//                File albumDirectory=new File(context.getFilesDir(),albumPath);
//                albumDirectory.mkdir();

                Vector<Integer> imageInAlbum = getPicturesInAlbum(i);
                for(int j: imageInAlbum) {
                    String picName = String.valueOf(j)+".jpg";
                    downloadImaeFromFirebaseStorage(context,userFolder,albumPath,picName);
                    Log.d(TAG,"saving image in folder: "+albumPath);
                }
            }
        }

//        while(count<total){}
//        MainActivity.refreshingChat = false;
        return;
    }

    static void downloadImaeFromFirebaseStorage(final Context context,File file, String chatFolder , final String imageName){
        StorageReference islandRef = storageRef.child(imageName);

        File chatDirectory=new File(file,chatFolder);

        if(!chatDirectory.exists()) {
            Log.d(TAG, "creating new folder for downloading image");
            chatDirectory.mkdir();
        }
        File mypath = new File(chatDirectory,imageName);

        if(mypath.exists()){
//            count++;
//            Log.d(TAG, "count increased: "+count);
//            Toast.makeText(context, "File already exists: "+mypath.getName(), Toast.LENGTH_SHORT).show();
        }
        else {

            islandRef.getFile(mypath).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
//                    adapter.notifyDataSetChanged();
//                    count++;
//                    Log.d(TAG, "count increased: "+count);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
//                    count++;
//                    Log.d(TAG, "count increased: "+count);
                    Toast.makeText(context, "Could not download image " + imageName, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    static String getEmailOfUser(){
        return mAuth.getCurrentUser().getEmail();
    }

    static public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    static void getImagesInFolder(File folder, ArrayList<File> files, ArrayList<Bitmap> images, ArrayList<String> names){
        if(folder.isDirectory()){
            for(File fs: folder.listFiles()){
                Log.d(TAG, fs.getAbsolutePath());
                if(!fs.isDirectory() && fs.getAbsolutePath().endsWith(".jpg")) {
                    String imagePath = fs.getAbsolutePath();
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    images.add(0,myBitmap);
                    names.add(0,fs.getName());
                    files.add(0,fs);
                }
            }
        }
    }

    static void getAlbumsInFolder(File folder, ArrayList<File> albums){
        if(folder.isDirectory()){
            for(File fs: folder.listFiles()){
//                Log.d(TAG, fs.getAbsolutePath());
                if(fs.isDirectory() && fs.getName().startsWith("album_")){
                    albums.add(0,fs);
                }
            }
        }
    }

    static void saveImageOnFirebaseStorage(final Context context, Bitmap b, int id){
        String imageName = String.valueOf(id)+".jpg";
        StorageReference imageRef = storageRef.child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(context, "Could not save Image on cloud!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static int getsUserIdFromEmailId(String m){
        Vector<String> v = new Vector<>();
        v.add(m);
        Vector<Integer> uids = getUseridsFromEmailids(v);
//        Random r = new Random();
//        return r.nextInt();
//        return 0;
        if(uids.size()==0){
            return -1;
        }
        return uids.get(0);
    }
//
//    static void addUserToGroup(int Uid, int chatID, boolean isActive){
//
//    }
//
//    static Vector<String> getPicturesInGroup(int gid){
//        return null;
//    }
//
//    static Vector<String> getGroupsOfUser(int Uid){
//        return new Vector<>();
//    }
//
//    static int createAlbumServer(String albumName, int Uid){
//        return 1;
//    }

    public static String getAlbumNameFromId(int id){
        return "";
    }

    public static Vector<String> getUsersInGroup(int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getUsersInGroup");
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));
            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            if(jarray==null)System.out.println("its a null");
            Vector<String> answer = new Vector<String>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add((String)jarray.get(i));
            }

            // boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<String>());
    }

    private static final String serverName = "52.90.143.1";
    // private static final String serverName = "localhost";
    private static final int port = 8500;

    public static byte[] applyFilter(byte[] imageByteArray, String filterCode) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            // ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.flush();
            System.out.println("Done making ois and oos");
            // OutputStream outToServer = client.getOutputStream();
            // ObjectOutputStream out = new ObjectOutputStream(outToServer);

            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // ImageIO.write( image, "jpg", baos );
            // baos.flush();
            // byte[] imageInByte = baos.toByteArray();
            // baos.close();
            String imageDataString = android.util.Base64.encodeToString(imageByteArray,android.util.Base64.NO_WRAP);

            JSONObject obj = new JSONObject();
            obj.put("Function","applyFilter");
            obj.put("image",imageDataString);
            obj.put("filterCode",filterCode);


            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            // JSONObject json = (JSONObject) parser.parse(objstr);
            // System.out.println(json.get("Function"));

            // String ret_imageDataString = (String)json.get("image");
            // byte[] ret_imageByteArray = Base64.getDecoder().decode(ret_imageDataString);
            // return ret_imageByteArray;

            // outputStream.write(objstr);
            // outputStream.flush()
            oos.flush();
            System.out.println("next line I write");
            oos.writeObject(objstr);

            oos.flush();
            // oos.reset();
            // oos.close();
            System.out.println("sent to server, awaiting response");
            // InputStream inFromServer = client.getInputStream();
            // ObjectInputStream in = new ObjectInputStream(inFromServer);

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

            System.out.println("done talking to server");
            String objrecvd = (String)ois.readObject();
            // ois.close();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));
            client.close();
            try {
                String answer = (String) jsonrecvd.get("answer");
                byte[] ret_imageByteArray =  android.util.Base64.decode(answer,android.util.Base64.NO_WRAP);
                return ret_imageByteArray;
            }
            catch (Exception e){
                return null;
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int createUser(String emailId, String userName) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","createUser");
            obj.put("emailId",emailId);
            obj.put("userName",userName);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            int answer = (int)(long)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Vector<Integer> getUseridsFromEmailids(Vector<String> emailIds) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getUseridsFromEmailids");
            JSONArray jsArray = new JSONArray();
            for (int i = 0; i < emailIds.size(); i++) {
                jsArray.add(emailIds.get(i));
            }
            obj.put("emailIds",jsArray);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            // JSONArray jarray = (JSONArray)json.get("userIds");
            // if(jarray!=null)System.out.println("yaaay, it is not null");
            // else System.out.println("booo! it is null");

            // int n = jarray.size();
            // System.out.println(n);


            // int[] usid = new int[n];
            // for(int i=0;i<n;i++)
            // {
            //    usid[i] = (int)(long)jarray.get(i);
            // }
            // System.out.println(usid[2]);


            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            if(jarray==null)System.out.println("its a null");
            Vector<Integer> answer = new Vector<Integer>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add(new Integer((int)(long)jarray.get(i)));
            }

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<Integer>());
    }

    public static int createGroup(Vector<Integer> userIds, int creatorUserId, String groupName) {
        try {

            Log.d(TAG,"createGroup: "+userIds.size());

            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","createGroup");
            JSONArray jsArray = new JSONArray();
            for (int i = 0; i < userIds.size(); i++) {
                jsArray.add(userIds.get(i));
            }
            obj.put("userIds",jsArray);
            obj.put("creatorUserId",creatorUserId);
            obj.put("groupName",groupName);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            JSONArray jarray = (JSONArray)json.get("userIds");
            // if(jarray!=null)System.out.println("yaaay, it is not null");
            // else System.out.println("booo! it is null");

            // int n = jarray.size();
            // System.out.println(n);


            // int[] usid = new int[n];
            // for(int i=0;i<n;i++)
            // {
            // 	usid[i] = (int)(long)jarray.get(i);
            // }
            // System.out.println(usid[2]);


            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            int answer = (int)(long)jsonrecvd.get("answer");
            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean addUserToGroup(int userId, int groupId, boolean isActive) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","addUserToGroup");
            obj.put("userId",userId);
            obj.put("groupId",groupId);
            obj.put("isActive",isActive);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean removeUserFromGroup(int userId, int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","removeUserFromGroup");
            obj.put("userId",userId);
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setGroupActive(int userId, int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","setGroupActive");
            obj.put("userId",userId);
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setGroupInactive(int userId, int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","setGroupInactive");
            obj.put("userId",userId);
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<String> getGroupsOfUser(int userId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getGroupsOfUser");
            obj.put("userId",userId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));
            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            if(jarray==null)System.out.println("its a null");
            Vector<String> answer = new Vector<String>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add((String)jarray.get(i));
            }

            // boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<String>());
    }

    public static int uploadPicture(int userId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","uploadPicture");
            obj.put("userId",userId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            int answer = (int)(long)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean sharePictureToGroup(int picId, int userId, int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","sharePictureToGroup");
            obj.put("picId",picId);
            obj.put("userId",userId);
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<String> getPicturesInGroup(int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getPicturesInGroup");
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));
            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            Vector<String> answer = new Vector<String>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add((String)jarray.get(i));
            }

            // boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<String>());
    }

    public static int createAlbumServer(String albumName, int userId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","createAlbumServer");
            obj.put("albumName",albumName);
            obj.put("userId",userId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            int answer = (int)(long)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean addPicturesToAlbum(Vector<Integer> picIds, int albumId) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","addPicturesToAlbum");
            JSONArray jsArray = new JSONArray();
            for (int i = 0; i < picIds.size(); i++) {
                jsArray.add(picIds.get(i));
            }
            obj.put("picIds",jsArray);
            obj.put("albumId",albumId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            JSONArray jarray = (JSONArray)json.get("userIds");
            // if(jarray!=null)System.out.println("yaaay, it is not null");
            // else System.out.println("booo! it is null");

            // int n = jarray.size();
            // System.out.println(n);


            // int[] usid = new int[n];
            // for(int i=0;i<n;i++)
            // {
            //    usid[i] = (int)(long)jarray.get(i);
            // }
            // System.out.println(usid[2]);


            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");
            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<Integer> getPicturesInAlbum(int albumId) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getPicturesInAlbum");

            obj.put("albumId",albumId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            // JSONArray jarray = (JSONArray)json.get("userIds");
            // if(jarray!=null)System.out.println("yaaay, it is not null");
            // else System.out.println("booo! it is null");

            // int n = jarray.size();
            // System.out.println(n);


            // int[] usid = new int[n];
            // for(int i=0;i<n;i++)
            // {
            //    usid[i] = (int)(long)jarray.get(i);
            // }
            // System.out.println(usid[2]);


            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            if(jarray==null)System.out.println("its a null");
            Vector<Integer> answer = new Vector<Integer>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add(new Integer((int)(long)jarray.get(i)));
            }

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<Integer>());
    }

    public static boolean shareAlbumWithGroup(int albumId, int groupId) {

        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","shareAlbumWithGroup");
            obj.put("albumId",albumId);
            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            System.out.println(json.get("Function"));



            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            boolean answer = (boolean)jsonrecvd.get("answer");

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Vector<Integer> getAlbumsInGroup(int groupId) {
        try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress());


            OutputStream outToServer = client.getOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(outToServer);


            JSONObject obj = new JSONObject();
            obj.put("Function","getAlbumsInGroup");

            obj.put("groupId",groupId);

            String objstr = obj.toString();
            System.out.println(objstr);
            // JSONObject newobj = new JSONObject(objstr);


            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(objstr);
            // JSONArray jarray = (JSONArray)json.get("userIds");
            // if(jarray!=null)System.out.println("yaaay, it is not null");
            // else System.out.println("booo! it is null");

            // int n = jarray.size();
            // System.out.println(n);


            // int[] usid = new int[n];
            // for(int i=0;i<n;i++)
            // {
            //    usid[i] = (int)(long)jarray.get(i);
            // }
            // System.out.println(usid[2]);


            out.writeObject(objstr);

            System.out.println("sent to server, awaiting response");
            InputStream inFromServer = client.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            System.out.println("done talking to server");
            String objrecvd = (String)in.readObject();
            System.out.println("Server says \n" + objrecvd);
            JSONObject jsonrecvd = (JSONObject) parser.parse(objrecvd);
            System.out.println(jsonrecvd.get("answer"));

            JSONArray jarray = (JSONArray)jsonrecvd.get("answer");
            if(jarray==null)System.out.println("its a null");
            Vector<Integer> answer = new Vector<Integer>();
            for(int i=0;i<jarray.size();i++)
            {
                answer.add(new Integer((int)(long)jarray.get(i)));
            }

            client.close();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (new Vector<Integer>());
    }
}
