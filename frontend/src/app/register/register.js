"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { useForm } from 'react-hook-form'
import { Container } from '@mui/material';
import axios from 'axios'
import { useState } from 'react';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import NextLink from 'next/link'
import { useGlobalContext } from '../store/store';
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';

export default function Register() {

    const { push } = useRouter();
    const {_, setUser} = useGlobalContext()
    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);
    const { handleSubmit, register, formState: { errors } } = useForm();
    const [errorMessage, setErrorMessage] = useState('');
    const [showErrorAlert, setShowErrorAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)
    const onSubmit = (data) => {
        setUser({
          email: data.email
        })
        axios.post('http://localhost:8080/api/auth/register', {
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            password: data.password,
            role: 'USER'
        })
        .then(res => {
          setShowSuccessAlert(true)
          setTimeout(() => {
            push('/user')
          }, 2500 ) 
        })
        .catch(err => {setError(err.response.data.errorMessage); setShowErrorAlert(true)})

        
    };
    return (
    <Container Container component="main" maxWidth="xs">
      <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8}}><AlertTitle>Account registered successfully!</AlertTitle> You will be redirected shortly.</Alert>
      <Alert  severity="error" onClose={() => {setShowErrorAlert(false)}} sx={{display: showErrorAlert ? "flex" : "none"}}>{errorMessage}</Alert>
        <Box
            sx={{
              marginTop: 1,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: '#4caf50' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign up
            </Typography>
            <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
              <TextField
                {...register('firstName', { required: 'First name is required' })}
                color="success"
                margin="normal"
                required
                fullWidth
                name="firstName"
                label="First name"
                id="firstName"
                error={!!errors.firstName}
                helperText={errors.firstName?.message}
              />
              <TextField
                {...register('lastName', { required: 'Last name is required' })}
                color="success"
                margin="normal"
                required
                fullWidth
                name="lastName"
                label="Last name"
                id="lastName"
                error={!!errors.lastName}
                helperText={errors.lastName?.message}
              />
              <TextField
                {...register('email', {
                  required: 'Email address is required',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                    message: 'Invalid email address'
                  }
                })}
                color="success"
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
                {...register('password', {
                  required: 'Password is required',
                  minLength: {
                    value: 8,
                    message: 'Password must be at least 8 characters long'
                  }
                })}
                color="success"
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type={showPassword ? "text" : "password"}
                InputProps={{ 
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}

                      >
                        {showPassword ? <Visibility /> : <VisibilityOff />}
                      </IconButton>
                    </InputAdornment>)
                }}
                id="password"
                autoComplete="current-password"
                error={!!errors.password}
                helperText={errors.password?.message}
              />
              <Typography component="h1" variant="h5" color="error">
                {errorMessage}
              </Typography>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2, bgcolor:"#4caf50", '&:hover':{bgcolor:"#4caf50 "} }}
              >
                Register
              </Button>
              <Grid container>
                <Grid item xs>
          
                </Grid>
                <Grid item>
                  <Link component={NextLink} href='/login' variant="body2">
                      Already have an account? Log in
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
      </Container>
    )
}