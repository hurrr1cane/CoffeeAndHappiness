import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import Modal from "@mui/material/Modal";
import { styled } from "@mui/material/styles";
import { Stack, IconButton, Fab } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import DoneIcon from "@mui/icons-material/Done";
import Rating from "@mui/material/Rating";
import axios from "axios";
import { useState } from "react";

export default function ReviewModal({ open, id, setOpen, token, reset }) {


    const [rating, setRating] = useState(0)

    const [comment, setComment] = useState("")

    const handleSubmit = () => {
        
        axios.post(`http://localhost:8080/api/review/cafe/${id}`, 
        {
            
            rating: rating,
            comment: comment
            
        }, 
        {
            headers: {
                "Accept":"*/*",
                "Access-Control-Allow-Origin": "http://localhost:3000",
                "Authorization": `Bearer ${token}` 
            }
        }
        )
        .then(res => {console.log(res); setOpen(false); reset()})
        .catch(err => console.log(err))
    }
    

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
    
      const handleClose = () => setOpen(false);
    
      const CustomTextField = styled(TextField)({
        "& .MuiInput-root::after": {
          borderBottom: "2px solid #66bb69",
        },
      });
    
      return (
        <>
          <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Stack direction="row" justifyContent="space-between">
            <Typography id="modal-modal-title" variant="h6" component="h2">
              Add review
            </Typography>
            <IconButton onClick={handleClose}>
              <CloseIcon sx={{ color: "black" }} />
            </IconButton>
          </Stack>
          <Typography component="legend">Add rating: </Typography>
          <Rating
            sx = {{right: "3px"}}
            name="simple-controlled"
            value={rating}
            onChange={(event, newValue) => {
                event.preventDefault()
                setRating(newValue);
            }}
            />
          <TextField
            sx={{ mt: 3}}
            color="success"
            fullWidth
            id="standard-multiline-flexible"
            placeholder="Leave your review here..."
            value={comment} 
            onChange={(event) => setComment(event.target.value)} 
            multiline
            maxRows={7}
            variant="standard"
          />
          <Fab
          onClick={handleSubmit}
          variant="extended"
            sx={{
              alignSelf: "center",
              mt: 3,
              color: "black",
              bgcolor: "#c6ffb3",
              ":hover": {
                bgcolor: "#c6ffb3",
              },
            }}
          >
            <DoneIcon sx={{mr:1}}/>
            Add review
          </Fab>
        </Box>
      </Modal>
        </>
      );
    }
