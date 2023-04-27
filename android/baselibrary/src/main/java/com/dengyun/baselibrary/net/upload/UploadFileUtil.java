package com.dengyun.baselibrary.net.upload;

import android.app.AlertDialog;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.net.NetApi;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.callback.JsonCallback;

import com.google.gson.Gson;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多文件依次上传
 * Created by seven on 2016/12/21.
 */

public class UploadFileUtil {
    private FragmentActivity context;
    private final TextView tvDownloadSize;
    private final TextView tvNetSpeed;
    private final TextView tvProgress;
    private final ProgressBar pbDialog;
    private final TextView tvDialogUploadnum;
    private final AlertDialog alertDialog;
    private  int uploadNum;
    private final String uploadUrl;
    private  boolean isCancleUpdate;
    private final ArrayList<String> uploadUrlList;

    public UploadFileUtil(FragmentActivity context,String uploadUrl){
        this.context = context;
        final AlertDialog.Builder uploadDialog= new AlertDialog.Builder(context);
        View view = context.getLayoutInflater().inflate(R.layout.base_dialog_upload, null);
        uploadDialog.setView(view);
        uploadDialog.setCancelable(false);//设置点击外面不消失

        tvDownloadSize = (TextView) view.findViewById(R.id.tv_dialog_tvDownloadSize);
        tvNetSpeed = (TextView) view.findViewById(R.id.tv_NetSpeed);
        tvProgress = (TextView) view.findViewById(R.id.tv_Progress);
        pbDialog = (ProgressBar) view.findViewById(R.id.pb_dialog);
        tvDialogUploadnum = (TextView) view.findViewById(R.id.tv_dialog_uploadnum);
        Button btnCancle = (Button) view.findViewById(R.id.btn_cancle_update);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancleUpdate = true;
                alertDialog.dismiss();
            }
        });
        alertDialog = uploadDialog.create();
        uploadNum = 1;
        isCancleUpdate = false;
        this.uploadUrl = uploadUrl;
        uploadUrlList = new ArrayList<>();
    }
    public  void uploadString(final List<String> picPaths, final OnUploadSuccessListener i_uploadSuccess){
        List<File> files = new ArrayList<>();
        for(int i = 0;i<picPaths.size();i++){
            File file = new File(picPaths.get(i));
            files.add(file);
        }
        uploadFile(files,i_uploadSuccess);
    }

    public  void uploadFile(final List<File> files, final OnUploadSuccessListener i_uploadSuccess){
        NetOption netOption = NetOption.newBuilder(uploadUrl)
                .activity(context)
                .isShowDialog(false)
                .build();
        NetApi.upFileData(netOption, files.get(uploadNum - 1), new JsonCallback<String>(netOption) {
            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                if (uploadNum == 1) {
                    alertDialog.show();
                }
                tvDialogUploadnum.setText(uploadNum + "/" + files.size());
            }

            @Override
            public void onSuccess(Response<String> response) {
                //上传成功
                Logger.d("------updata------上传成功," + response.body());
                Gson gson = new Gson();
                UploadBean upload = gson.fromJson(response.body(), UploadBean.class);
                uploadUrlList.add(upload.data.fileUrl);
                if (uploadNum < files.size()) {
                    uploadNum++;
                    uploadFile(files,i_uploadSuccess);
                } else {
                    if (!isCancleUpdate) {
                        alertDialog.dismiss();
                        i_uploadSuccess.doUploadSuccess(uploadUrlList);
                    }
                }
            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                //这里回调上传进度(该回调在主线程,可以直接更新ui)
                String downloadLength = Formatter.formatFileSize(context.getApplicationContext(), progress.currentSize);
                String totalLength = Formatter.formatFileSize(context.getApplicationContext(), progress.totalSize);
                tvDownloadSize.setText(downloadLength + "/" + totalLength);
                String netSpeed = Formatter.formatFileSize(context.getApplicationContext(), progress.speed);
                tvNetSpeed.setText(netSpeed + "/S");
                tvProgress.setText((Math.round(progress.fraction * 10000) * 1.0f / 100) + "%");
                pbDialog.setProgress((int) (progress.fraction * 100));
            }
        });

    }
}
