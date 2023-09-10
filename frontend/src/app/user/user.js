"use client"

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

export default function User() {
  const { user, setUser, isDark } = useGlobalContext();
  const { width, height } = useWindowSize();
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(false); // Track if editing mode is enabled
  const [firstName, setFirstName] = useState(user?.firstName || "");
  const [lastName, setLastName] = useState(user?.lastName || "");
  const [updatedUser, setUpdatedUser] = useState({}); // Store updated user data

  useEffect(() => {
    axios
      .get("https://coffee-and-happiness-backend.azurewebsites.net/api/user/me", {
        headers: {
          Authorization: "Bearer " + user.token,
        },
      })
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
              orders: res.data.orders
        }));
      })
      .catch((err) => console.log(err));
  }, []);


  useEffect(() => {
    if (Date.now() - user.date > 86400 * 1000) { 
      console.log(Date.now(), user.date)
      axios.post("https://coffee-and-happiness-backend.azurewebsites.net/api/auth/refresh", {}, {
        headers: {
          Authorization: "Bearer " + user.refreshToken
        }
      })
      .then(res => {
        console.log(res);
      })
      .catch(err => {
        console.log(err.response.data);
        setUser(prev => ({
          ...prev,
          refreshToken: err.response.data.refreshToken,
          accessToken: err.response.accessToken
        }))
      });
    }
  }, []);

  const handleEditClick = () => {
    // Enable editing mode
    setEditing(true);
  };

  const handleSaveClick = () => {
    // Disable editing mode
    setEditing(false);

    // Update user data
    axios
      .put(
        "https://coffee-and-happiness-backend.azurewebsites.net/api/user/update",
        {
          firstName: firstName,
          lastName: lastName,
          // ... (other fields to update)
        },
        {
          headers: {
            Authorization: "Bearer " + user.token,
          },
        }
      )
      .then((res) => {
        // Update the user context with the new data
        setUser((prev) => ({
          ...prev,
          firstName: res.data.firstName,
          lastName: res.data.lastName,
          // ... (other fields)
        }));
      })
      .catch((err) => console.log(err));
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
            <Image
              className={styles.image}
              src={user?.imageUrl ? imageUrl : "/user.png"}
              width={100}
              height={100}
              alt="avatar image"
            />
            {editing ? (
              <>
                <TextField
                  sx = {{mb: 1}}
                  color="success"
                  label="First Name"
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                  variant="outlined"
                />
                <TextField
                  color="success"
                  label="Last Name"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  variant="outlined"
                />
              </>
            ) : (
              <h1>
                {user?.firstName} {user?.lastName}
              </h1>
            )}
          </div>
          <div className={styles.info}>
            <h2>Bonus points: {user?.bonusPoints}</h2>
            <h2>Email: {user?.email}</h2>
            {user.phoneNumber ? (
              <h2>Phone number: {user?.phoneNumber}</h2>
            ) : (
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
                <LocalPhoneIcon sx={{ mr: 1 }} /> Add phone number
              </Button>
            )}
            {editing && (
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
                Save
              </Button>
            )}
          </div>
        </section>
        <Orders />
      </div>
    </>
  );
}