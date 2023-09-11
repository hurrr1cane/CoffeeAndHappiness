"use client";

import { useState, useEffect } from "react";
import axios from "axios";
import { Button, TextField } from "@mui/material";
import { useGlobalContext } from "../store/store";
import styles from "./user.module.scss";
import Image from "next/image";
import { useRouter } from "next/navigation";
import LocalPhoneIcon from "@mui/icons-material/LocalPhone";
import Orders from "./orders";
import useWindowSize from "../menu/dish/[id]/Reviews/useWindow";
import Edit from "@mui/icons-material/Edit";
import { Save } from "@mui/icons-material";
import { Stack } from "@mui/material";
import { Close } from "@mui/icons-material";
import { Tooltip } from "@mui/material";
import ImageIcon from '@mui/icons-material/Image';

export default function User() {

  const { user, setUser, isDark } = useGlobalContext();
  const { width, height } = useWindowSize();

  const [editing, setEditing] = useState(false);
  const [firstName, setFirstName] = useState(user.firstName || "");
  const [lastName, setLastName] = useState(user.lastName || "");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [addingPhoneNumber, setAddingPhoneNumber] = useState(false);
  const [file, setFile] = useState(null)

  const handleFileChange = (newFile) => {
    setFile(newFile)
    console.log(newFile)
  }

  const handleUploadClick = () => {
    if (file) {
      const formData = new FormData();
      formData.append("image", file);

      axios
        .post(
          "https://coffee-and-happiness-backend.azurewebsites.net/api/user/me/image/add",
          formData,
          {
            headers: {
              Authorization: "Bearer " + user.token,
              "Content-Type": "multipart/form-data",
            },
          }
        )
        .then((res) => {
          console.log("File uploaded successfully", res);
        })
        .catch((error) => {
          console.error("Error uploading file", error);
        })
        .finally(() => {
          setFile(null)
        });

        
    }
  };
  

  useEffect(() => {
    axios
      .get(
        "https://coffee-and-happiness-backend.azurewebsites.net/api/user/me",
        {
          headers: {
            Authorization: "Bearer " + user.token,
          },
        }
      )
      .then((res) => {
        setUser((prev) => ({
          ...prev,
          firstName: res.data.firstName,
          lastName: res.data.lastName,
          imageUrl: res.data.imageUrl,
          role: res.data.role,
          bonusPoints: res.data.bonusPoints,
          orders: res.data.orders,
          id: res.data.id,
          orders: res.data.orders,
        }));
      })
      .catch((err) => console.log(err));
  }, []);

  useEffect(() => {
    if (Date.now() - user.date > 86400 * 1000) {
      console.log(Date.now(), user.date);
      axios
        .post(
          "https://coffee-and-happiness-backend.azurewebsites.net/api/auth/refresh",
          {},
          {
            headers: {
              Authorization: "Bearer " + user.refreshToken,
            },
          }
        )
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err.response.data);
          setUser((prev) => ({
            ...prev,
            refreshToken: err.response.data.refreshToken,
            accessToken: err.response.accessToken,
          }));
        });
    }
  }, []);

  const handleEditClick = () => {
    // Enable editing mode
    setEditing(true);
    setAddingPhoneNumber(false);
  };

  const handleSaveClick = () => {
    setEditing(false);
    setAddingPhoneNumber(false)
    // Update user data
    if (phoneNumber.length == 0) {
      axios
      .put(
        "https://coffee-and-happiness-backend.azurewebsites.net/api/user/me/edit",
        {
          firstName: firstName,
          lastName: lastName,
        },
        {
          headers: {
            Authorization: "Bearer " + user.token,
          },
        }
      )
      .then((res) => {
        console.log(res);
        setUser((prev) => ({
          ...prev,
          firstName: firstName,
          lastName: lastName,
        }));
      })
      .catch((err) => console.log(err));
    }
      else {
        axios
        .put(
          "https://coffee-and-happiness-backend.azurewebsites.net/api/user/me/edit",
          {
            phoneNumber: phoneNumber
          },
          {
            headers: {
              Authorization: "Bearer " + user.token,
            },
          }
        )
        .then((res) => {
          console.log(res);
          setUser((prev) => ({
            ...prev,
            phoneNumber: phoneNumber
          }));
        })
        .catch((err) => console.log(err));}
      }
  
  const handleStopClick = () => {
    // Disable editing mode
    setEditing(false);
    setAddingPhoneNumber(false)
  };

  const { push } = useRouter();

  if (!user) {
    push("/login");
    return null; // Render nothing while waiting for user data
  }

  return (
    <>
      {height > 500 ? (
        <h1 style={{ fontWeight: 400, color: isDark ? "#CCCCCC" : "black" }}>
          Welcome, {user?.firstName} {user?.lastName}
        </h1>
      ) : (
        <h1 style={{ fontWeight: 400, color: isDark ? "#CCCCCC" : "black" }}>
          Welcome, <br />
          {user?.firstName} {user?.lastName}{" "}
        </h1>
      )}
      <div className={`${styles.container} ${isDark ? styles.dark : ""}`}>
        <section className={styles.main}>
          <div className={styles.user}>
          <Tooltip title="Add a profile photo" placement="top"  arrow>
              <label htmlFor="fileInput">
                <Image
                  style={{borderRadius:"100px"}}
                  className={styles.image}
                  src={user?.imageUrl ? user.imageUrl : "/user.png"}
                  width={100}
                  height={100}
                  alt="avatar image"
                />
              </label>
            </Tooltip>
            {/* Add the file input with an id */}
            <input
              type="file"
              id="fileInput"
              accept="image/*"
              style={{ display: "none" }}
              onChange={(e) => handleFileChange(e.target.files[0])}
            />
            <h1>
              {user?.firstName} {user?.lastName}
            </h1>
          </div>
          <div className={styles.info}>
            <h2>Bonus points: {user?.bonusPoints}</h2>
            <h2>Email: {user?.email}</h2>
            <Stack direction="column">
              {user.phoneNumber ? (
                <h2>Phone number: {user?.phoneNumber}</h2>
              ) : (
                <Button
                  fullWidth
                  variant="contained"
                  onClick={() => {
                    setAddingPhoneNumber(true);
                  }}
                  sx={{
                    display: (editing || addingPhoneNumber) && "none",
                    mt: 1,
                    mb: 1,
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": {
                      bgcolor: isDark ? "#388E3C" : "#4caf50",
                    },
                  }}
                >
                  <LocalPhoneIcon sx={{ mr: 1 }} /> Add phone number
                </Button>
              )}
              {addingPhoneNumber && (
                <TextField
                  value={phoneNumber}
                  onChange={(event) => {
                    setPhoneNumber(event.target.value);
                  }}
                  sx={{
                    input: {
                      color: isDark && "#CCCCCC",
                    },
                  }}
                  id="standard-basic"
                  color="success"
                  label="Phone number"
                  placeholder="Input your phone number"
                  variant="standard"
                />
              )}
              {!editing && !addingPhoneNumber && (
                <Button
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 1,
                    mb: 1,
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": {
                      bgcolor: isDark ? "#388E3C" : "#4caf50",
                    },
                  }}
                  onClick={handleEditClick}
                >
                  <Edit sx={{ mr: 1 }} /> Edit Profile
                </Button>
              )}
            </Stack>
            {editing && (
              <Stack direction="column">
                <TextField
                  value={firstName}
                  onChange={(event) => {
                    setFirstName(event.target.value);
                  }}
                  sx={{
                    input: {
                      color: isDark && "#CCCCCC",
                    },
                  }}
                  id="standard-basic"
                  color="success"
                  label="First name"
                  placeholder="Input new first name"
                  variant="standard"
                />
                <TextField
                  value={lastName}
                  onChange={(event) => {
                    setLastName(event.target.value);
                  }}
                  sx={{
                    input: {
                      color: isDark && "#CCCCCC",
                    },
                  }}
                  id="standard-basic"
                  color="success"
                  label="Last name"
                  placeholder="Input new last name"
                  variant="standard"
                />
              </Stack>
            )}
            
            {(editing || addingPhoneNumber) && (
              <Stack direction="column">
                <Button
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 1,
                    mb: 1,
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": {
                      bgcolor: isDark ? "#388E3C" : "#4caf50",
                    },
                  }}
                  onClick={handleSaveClick}
                >
                  <Save sx={{ mr: 1 }} /> Save
                </Button>
                <Button
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 1,
                    mb: 1,
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": {
                      bgcolor: isDark ? "#388E3C" : "#4caf50",
                    },
                  }}
                  onClick={handleStopClick}
                >
                  <Close sx={{ mr: 1 }} /> Cancel
                </Button>
              </Stack>
            )}
            {
              file && (
                <Button
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 1,
                    mb: 1,
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": {
                      bgcolor: isDark ? "#388E3C" : "#4caf50",
                    },
                  }}
                  onClick={handleUploadClick}
                >
                  <ImageIcon sx={{ mr: 1 }} /> Submit profile picture
                </Button>
              )
            }
          </div>
        </section>
        <Orders />
      </div>
    </>
  );
}
