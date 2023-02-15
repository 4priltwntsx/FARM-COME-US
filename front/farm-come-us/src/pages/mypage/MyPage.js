import React, { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";
import MyPageHeader from "../../components/mypage/MyPageHeader";
import { fetchUpdateUserInfo } from "../../utils/api/user-http";
import { useDispatch } from "react-redux";
import { useSelector } from "react-redux";
import userSlice from "../../reduxStore/userSlice";
import axios from "axios";

const MyPage = (props) => {
  const [isEditting, setIsEditting] = useState(false);
  const user = useSelector((state) => {
    return state.userSlice.value;
  });
  const [userInfo, setUserInfo] = useState({
    ...user,
    imgSrc: "",
    uploadFile: "",
  });
  const [initUserInfo, setInitUserInfo] = useState({
    ...user,
    imgSrc: "",
    uploadFile: "",
  });

  useEffect(() => {
    const accessToken = sessionStorage.getItem("accessToken");
    axios
      .get(process.env.REACT_APP_API_SERVER_URL + "/api/v1/member/", {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          token: accessToken,
        },
      })
      .then((res) => {
        const { userInfo, userImage } = res.data;
        if (userImage) {
          userInfo["imgSrc"] = userImage.savedPath;
        }
        setUserInfo((prev) => {
          return { ...prev, ...userInfo };
        });
        userSlice.actions.login(userInfo);
        setInitUserInfo(userInfo);
      })
      .catch((err) => {
        console.error(err);
      });
  }, []);

  const toggleIsEditting = (e) => {
    e.preventDefault();
    setIsEditting((prev) => !prev);
  };

  const editInfoHandler = (e) => {
    e.preventDefault();
    fetchUpdateUserInfo(userInfo)
      .then(() => {
        alert("사용자 정보가 수정되었습니다.");
      })
      .catch((err) => {
        console.error(err);
      });
    setIsEditting((prev) => !prev);
  };

  const userInfoChangeHandler = (name, value) => {
    setUserInfo((prev) => {
      return {
        ...prev,
        [name]: value,
      };
    });
  };

  const cancelInfoEditHandler = () => {
    setUserInfo((prev) => {
      return {
        ...initUserInfo,
      };
    });

    setIsEditting((prev) => !prev);

    alert("수정이 취소되었습니다.");
  };

  return (
    <div>
      <MyPageHeader
        profileImg={userInfo.imgSrc}
        userInfo={userInfo}
        isEditting={isEditting}
        userInfoChangeHandler={userInfoChangeHandler}
      />
      <Outlet
        context={{
          userInfo: userInfo,
          isEditting,
          toggleIsEditting,
          editInfoHandler,
          userInfoChangeHandler,
          cancelInfoEditHandler,
        }}
      />
    </div>
  );
};

export default MyPage;