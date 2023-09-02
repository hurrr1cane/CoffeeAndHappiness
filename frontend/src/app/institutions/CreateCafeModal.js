"use client"

import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import { styled } from "@mui/material/styles";
import { Stack, IconButton, Fab } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import EditIcon from "@mui/icons-material/Edit"
import axios from "axios";
import { useState } from "react";
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import { useGlobalContext } from "../store/store";

export default function CreateCafeModal({open, setOpen}) {


    const defaultCafe = {
        "locationEN": "",
        "locationUA": "",
        "imageUrl": "",
        "phoneNumber": ""
      }

    const {user, _} = useGlobalContext()

    const style = {
        position: "absolute",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%)",
        width: 650,
        bgcolor: "white",
        boxShadow: 24,
        p: 4,
        borderRadius: 1,
      };
    
      const [formData, setFormData] = useState(defaultCafe);

      const handleChange = (key, value) => {
        setFormData({ ...formData, [key]: value });
      };

      const handleSubmit = () => {
        axios.put(`http://localhost:8080/api/cafe/`, formData, {
            headers: {
                Authorization: "Bearer " + user.token
            }
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))
        handleClose();
      };

      const handleClose = () => setOpen(false);
    
      const CustomTextField = styled(TextField)({
        "& .MuiInput-root::after": {
          borderBottom: "2px solid #66bb69",
        },
      });
    
      
      const keysToDisplay = Object.keys(defaultCafe);
      const halfKeysLength = Math.ceil(keysToDisplay.length / 2);
      const firstColumnKeys = keysToDisplay.slice(0, halfKeysLength);
      const secondColumnKeys = keysToDisplay.slice(halfKeysLength);

      return (
          <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Stack direction="row" justifyContent="space-between">
            <Typography id="modal-modal-title" variant="h6" component="h2">
              Add cafe
            </Typography>
            <IconButton onClick={handleClose}>
              <CloseIcon sx={{ color: "black" }} />
            </IconButton>
          </Stack>
          
          <Grid container spacing={2}>
          <Grid item xs={6}>
            <Paper sx={{ p: 2 }}>
              {firstColumnKeys.map((key) => (
                <TextField
                  key={key}
                  sx={{ mt: 2 }}
                  color="success"
                  fullWidth
                  label={key}
                  value={formData[key]}
                  onChange={(e) => handleChange(key, e.target.value)}
                />
              ))}
            </Paper>
          </Grid>
          <Grid item xs={6}>
            <Paper sx={{ p: 2 }}>
              {secondColumnKeys.map((key) => (
                <TextField
                  key={key}
                  sx={{ mt: 2 }}
                  color="success"
                  fullWidth
                  label={key}
                  value={formData[key]}
                  onChange={(e) => handleChange(key, e.target.value)}
                />
              ))}
            </Paper>
          </Grid>
        </Grid>
          <Fab
          onClick={handleSubmit}
          variant="extended"
            sx={{
              alignSelf: "center",
              mt: 3,
              color: "black",
              bgcolor: "#4caf81",
              ":hover": {
                bgcolor: "#4caf81",
              },
            }}
          >
            <EditIcon sx={{mr:1}}/>
            Add cafe
          </Fab>
        </Box>
      </Modal>
      );
    }
