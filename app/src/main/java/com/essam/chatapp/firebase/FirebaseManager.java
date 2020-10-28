package com.essam.chatapp.firebase;

import com.essam.chatapp.models.User;
import com.essam.chatapp.utils.Consts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseManager {
    public enum UserAuthState{
        LOGGED_IN,
        LOGGED_OUT,
    }
    private static FirebaseManager instance;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference appUserDb;        //ChatApp/user/
    private DatabaseReference appChatDb;        //ChatApp/chat/
    private DatabaseReference mUserDb;          //ChatApp/user/uid/
    private DatabaseReference userChatDb;       //ChatApp/user/uid/chat/

    private FirebaseManager() {
        initFirebase();
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    private void initFirebase() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference rootDbReference = FirebaseDatabase.getInstance().getReference();
        appUserDb = rootDbReference.child(Consts.USER);
        appChatDb = rootDbReference.child(Consts.CHAT);

        if (isUserLoggedIn()){
            mUserDb = appUserDb.child(getMyUid());
            userChatDb = mUserDb.child(Consts.CHAT);
        }
    }

    public void updateUserAuthState(UserAuthState state){
        switch (state){
            case LOGGED_IN:
                logInUser();
                break;
            case LOGGED_OUT:
                signOutUser();
        }
    }

    private void logInUser(){
        initFirebase();
    }

    public boolean isUserLoggedIn() {
        return getFirebaseUser() != null;
    }

    public void signOutUser() {
        getFirebaseAuth().signOut();
        instance = null;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    public FirebaseUser getFirebaseUser() {
        return getFirebaseAuth().getCurrentUser();
    }

    public String getMyUid() {
        return getFirebaseUser().getUid();
    }

    public String getMyPhone() {
        return getFirebaseUser().getPhoneNumber();
    }

    public DatabaseReference getAppUserDb() {
        return appUserDb;
    }

    public DatabaseReference getAppChatDb() {
        return appChatDb;
    }

    /* -------------------------------------- Login ---------------------------------------------*/

    public void checkIfUserExistInDataBase(ValueEventListener eventListener){
        mUserDb.addListenerForSingleValueEvent(eventListener);
    }

    public void addCurrentUserToDatabase(User user){
        mUserDb.setValue(user);
    }

    /* -------------------------------------- Home Chat ----------------------------------------*/

    /**
     * Check if current user has any chat history
     */
    public void checkChatHistoryForCurrentUser (ValueEventListener eventListener){
        userChatDb.addListenerForSingleValueEvent(eventListener);
    }

    /**
     * fetch previous chats AND listen for new messages
     */
    public void getUserChatList(ChildEventListener eventListener){
        userChatDb.addChildEventListener(eventListener);
    }

    public void removeHomeChatListeners(ChildEventListener eventListener) {
        if (eventListener != null){
            userChatDb.removeEventListener(eventListener);
        }
    }

    /* ----------------------------------- One To One Chat -------------------------------------*/

    public DatabaseReference getReferenceToThisChat(String chatId){
        return userChatDb.child(chatId);
    }

}