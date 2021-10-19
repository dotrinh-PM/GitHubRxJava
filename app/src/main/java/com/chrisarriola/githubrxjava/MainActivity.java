package com.chrisarriola.githubrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.chrisarriola.githubrxjava.pojo.GitHubRepo;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private GitHubRepoAdapter adapter = new GitHubRepoAdapter();
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.list_view_repos);
        listView.setAdapter(adapter);
        getStarredRepos("dotrinhdev");

//        final EditText editTextUsername = (EditText) findViewById(R.id.edit_text_username);
//        final Button buttonSearch = (Button) findViewById(R.id.button_search);
//        buttonSearch.setOnClickListener(v -> {
//            final String username = editTextUsername.getText().toString();
//            if (!TextUtils.isEmpty(username)) {
//                getStarredRepos(username);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void getStarredRepos(String username) {
        Observable<List<GitHubRepo>> listObservable = GitHubClient.getInstance().getStarredRepos(username);
        subscription = listObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GitHubRepo>>() {
                    @Override
                    public void onNext(List<GitHubRepo> gitHubRepos) {
                        Log.d("TESSST", "In onNext()");
                        adapter.setGitHubRepos(gitHubRepos);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("TESSST", "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("TESSST", "In onError()");
                    }
                });
    }
}
