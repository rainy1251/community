package com.netease.nim.uikit.business.session;


import com.netease.nim.uikit.Contents;
import com.netease.nim.uikit.SPUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimMessageRevokeObserver;
import com.netease.nim.uikit.business.session.module.MsgForwardFilter;
import com.netease.nim.uikit.business.session.module.MsgRevokeFilter;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderTip;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import static com.netease.nim.uikit.impl.NimUIKitImpl.getRecentCustomization;

public class SessionHelper {



    public static void init() {
//        // 注册自定义消息附件解析器
//        NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());

//        // 注册各种扩展消息类型的显示ViewHolder
//        registerViewHolders();

        // 设置会话中点击事件响应处理
        setSessionListener();

//        // 注册消息转发过滤器
        registerMsgForwardFilter();

//        // 注册消息撤回过滤器



        registerMsgRevokeFilter();

//        // 注册消息撤回监听器
        registerMsgRevokeObserver();
        registerViewHolders();
//        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());
//
//        NimUIKit.setCommonTeamSessionCustomization(getTeamCustomization(null));

        NimUIKit.setRecentCustomization(getRecentCustomization());
    }
    private static void registerMsgRevokeObserver() {
        NIMClient.getService(MsgServiceObserve.class).observeRevokeMessage(new NimMessageRevokeObserver(), true);
    }

    private static void registerViewHolders() {
        NimUIKit.registerTipMsgViewHolder(MsgViewHolderTip.class);
    }

    private static void setSessionListener() {
//        SessionEventListener listener = new SessionEventListener() {
//            @Override
//            public void onAvatarClicked(Context context, IMMessage message) {
//                // 一般用于打开用户资料页面
//                if (message.getMsgType() == MsgTypeEnum.robot && message.getDirect() == MsgDirectionEnum.In) {
//                    RobotAttachment attachment = (RobotAttachment) message.getAttachment();
//                    if (attachment.isRobotSend()) {
//                        RobotProfileActivity.start(context, attachment.getFromRobotAccount());
//                        return;
//                    }
//                }
//                UserProfileActivity.start(context, message.getFromAccount());
//            }
//
//            @Override
//            public void onAvatarLongClicked(Context context, IMMessage message) {
//                // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
//            }
//
//            @Override
//            public void onAckMsgClicked(Context context, IMMessage message) {
//                // 已读回执事件处理，用于群组的已读回执事件的响应，弹出消息已读详情
//                AckMsgInfoActivity.start(context, message);
//            }
//        };

//        NimUIKit.setSessionListener(listener);
    }


    /**
     * 消息转发过滤器
     */
    private static void registerMsgForwardFilter() {
        NimUIKit.setMsgForwardFilter(new MsgForwardFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {



//                if (message.getMsgType() == MsgTypeEnum.custom && message.getAttachment() != null
//                        && (message.getAttachment() instanceof SnapChatAttachment
//                        || message.getAttachment() instanceof RTSAttachment
//                        || message.getAttachment() instanceof RedPacketAttachment)) {
//                    // 白板消息和阅后即焚消息，红包消息 不允许转发
//                    return true;
//                } else if (message.getMsgType() == MsgTypeEnum.robot && message.getAttachment() != null && ((RobotAttachment) message.getAttachment()).isRobotSend()) {
//                    return true; // 如果是机器人发送的消息 不支持转发
//                }
                return false;
            }
        });
    }

    /**
     * 消息撤回过滤器
     */
    private static void registerMsgRevokeFilter() {
        NimUIKit.setMsgRevokeFilter(new MsgRevokeFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                String fromAccount = message.getFromAccount();
                String currAccount = SPUtils.getString(Contents.IMAccoune);

//                if (fromAccount.equals(currAccount)) {
//
//                    return true;
//                }
//                if (message.getAttachment() != null
//                        && (message.getAttachment() instanceof AVChatAttachment
//                        || message.getAttachment() instanceof RTSAttachment
//                        || message.getAttachment() instanceof RedPacketAttachment)) {
//                    // 视频通话消息和白板消息，红包消息 不允许撤回
//                    return true;
//                } else if (DemoCache.getAccount().equals(message.getSessionId())) {
//                    // 发给我的电脑 不允许撤回
//                    return true;
//                }
                return false;
            }
        });
    }
}
