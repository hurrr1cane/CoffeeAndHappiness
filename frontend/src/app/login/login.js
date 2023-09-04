"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useForm } from 'react-hook-form'
import axios from 'axios';
import { useState } from 'react';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { NextLink } from 'next/link'
import { useGlobalContext } from '../store/store';
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';

export default function Login() {

    const { push } = useRouter();
    const {_, setUser} = useGlobalContext()
    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);
    const { handleSubmit, register, formState: { errors } } = useForm();
    const [error, setError] = useState("")
    const [showAlert, setShowAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)

    const onSubmit = (data) => {

        setUser({
          email: data.email
        })
        axios.post('http://localhost:8080/api/auth/login', {
            email:data.email,
            password: data.password
        })
        .then(res => {
            setUser(prev => (
              {...prev, token:res.data.accessToken, refreshToken: res.data.accessToken}
            ))
            setShowSuccessAlert(true)
            setTimeout(() => {
              push('/user')
            }, 1500)
            
        })
        .catch(err => {setError(err.response.data.errorMessage); setShowAlert(true)})

        
    }

    return (
        <Container component="main" maxWidth="xs">
          <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8, marginBottom: 1}}><AlertTitle>Logged in successfully</AlertTitle> You will be redirected shortly.</Alert>
          <Alert onClose={() => {setShowAlert(false)}} sx={{display: showAlert ? "flex" : "none"}}
           severity="error">{error}</Alert>
            <Box
              sx={{
                marginTop: 2,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
            >
              <Avatar sx={{ m: 1, bgcolor: '#4caf50' }}>
                <LockOutlinedIcon />
              </Avatar>
              <Typography component="h1" variant="h5">
                Log in
              </Typography>
              <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate sx={{ mt: 1 }}>
                <TextField
                  color="success"
                  {...register('email', {
                    required: 'Email address is required',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                      message: 'Invalid email address'
                    }
                  })}
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  error={!!errors.email}
                  helperText={errors.email?.message}
                />
                <TextField
                color="success"
                  {...register('password', {
                    required: 'Password is required',
                  })}
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type={showPassword ? "text" : "password"}
                  sx = {{
                    fieldset: {
                        outlineColor: "red"
                    }
                  }}
                  InputProps={{ 
                    endAdornment: (
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={handleClickShowPassword}
            
                        >
                          {showPassword ? <Visibility /> : <VisibilityOff />}
                        </IconButton>
                      </InputAdornment>)}}
                  id="password"
                  autoComplete="current-password"
                  error={!!errors.password}
                  helperText={errors.password?.message}
                />
                <FormControlLabel
                  control={<Checkbox value="remember" color="primary" />}
                  label="Remember me"
                />
                <Button
            
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, mb: 2, bgcolor:"#4caf50", '&:hover':{bgcolor:"#4caf50 "} }}
                >
                  Sign In
                </Button>
                <Grid container>
                  <Grid item xs>
                  <Link component={NextLink} href='/forgotpassword' variant='body2'>
                      Forgot password?
                    </Link>
                  </Grid>
                  <Grid item>
                    <Link component={NextLink} href='/register' variant='body2'>
                      Don't have an account? Register
                    </Link>
                  </Grid>
                </Grid>
              </Box>
            </Box>
        </Container>
    )
}