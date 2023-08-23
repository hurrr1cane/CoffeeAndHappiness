import AppBar from "@mui/material/AppBar";
import { Stack, Typography, Toolbar } from "@mui/material";
import Link from "next/link";
export default function Navbar() {
  return (
    <>
    <AppBar sx={{height: "8vh", mt: 2, width:"98vw", ml:"auto", mr:"auto", borderRadius:8}}>
        <Stack
          sx={{ height: "100%" }}
          direction="row"
          alignItems="center"
          justifyContent="space-between"
        >
          <Stack direction="row" alignItems="center">
            <Typography sx={{ m: 5 }}>
              <Link href="/">Home</Link>
            </Typography>
            <Typography sx={{ m: 5 }}>
              <Link href="/institutions">Institutions</Link>
            </Typography>
            <Typography sx={{ m: 5 }}>
              <Link href="/menu">Menu</Link>
            </Typography>
            <Typography sx={{ m: 5 }}>
              <Link href="/about-us">About us</Link>
            </Typography>
          </Stack>
          <Typography sx={{ m: 5 }}>
            <Link href="/user">User</Link>
          </Typography>
        </Stack>
    </AppBar>
    <Toolbar/>
    </>
  );
}
