<template>
   <div class="app-container">
      <el-row :gutter="20">
         <el-col :span="6" :xs="24">
            <el-card class="box-card">
               <template v-slot:header>
                 <div class="clearfix">
                   <span>个人信息</span>
                 </div>
               </template>
               <div>
                  <div class="text-center">
                     <userAvatar />
                  </div>
                  <ul class="list-group list-group-striped">
                     <li class="list-group-item">
                        <svg-icon icon-class="user" />账号
                        <div class="pull-right">{{ state.user.username }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="user" />用户昵称
                        <div class="pull-right">{{ state.user.nickname }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="phone" />手机号码
                        <div class="pull-right">{{ state.user.phone }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="email" />用户邮箱
                        <div class="pull-right">{{ state.user.email }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="user" />性别
                        <div class="pull-right">{{ genderStr }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="peoples" />所属角色
                        <div class="pull-right">{{ state.roles }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="date" />创建日期
                        <div class="pull-right">{{ state.user.createTime }}</div>
                     </li>
                  </ul>
               </div>
            </el-card>
         </el-col>
         <el-col :span="18" :xs="24">
            <el-card>
               <template v-slot:header>
                 <div class="clearfix">
                   <span>基础设置</span>
                 </div>
               </template>
               <el-tabs v-model="activeTab">
                  <el-tab-pane label="基本资料" name="userinfo">
                     <userInfo :user="state.user" />
                  </el-tab-pane>
                  <el-tab-pane label="修改密码" name="resetPwd">
                     <resetPwd />
                  </el-tab-pane>
               </el-tabs>
            </el-card>
         </el-col>
      </el-row>
   </div>
</template>

<script setup name="Profile">
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";


const { proxy } = getCurrentInstance();
const { sys_gender } = proxy.useDict("sys_gender");

const activeTab = ref("userinfo");
const genderStr = ref("");
const state = reactive({
  user: {},
  roles: {}
});


function getUser() {
  getUserProfile().then(response => {
    state.user = response.user;
    let rolesString = Array.from(response.roles).join("、 ");
    state.roles = rolesString;
    watch(()=>state.user.gender,()=>{
      sys_gender.value.forEach(obj => {
        if (obj.value == state.user.gender) {
          genderStr.value = obj.label;
        }
      });
    });

    genderStr.value = sys_gender.value.find(obj => obj.value == state.user.gender).label; 
  });
};

getUser();
</script>
