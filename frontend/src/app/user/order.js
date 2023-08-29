import Accordion from '@mui/material/Accordion';
import AccordionDetails from '@mui/material/AccordionDetails';
import AccordionSummary from '@mui/material/AccordionSummary';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Typography from '@mui/material/Typography';
import { Stack } from '@mui/material';

export default function Order({ expanded, name, id, date, handleChange }){

    

    return (
        <Accordion disableGutters sx={{
                flexGrow: 1,
                fontWeight: 500,
                
                }} onChange={handleChange(id)} expanded={expanded === id}>
            <AccordionSummary
          expandIcon={<ExpandMoreIcon />}
          aria-controls="panel1bh-content"
          id="panel1bh-header"
        >
          <Typography variant='h6' fontWeight="400" sx={{ width: '33%', flexShrink: 0 }}>
            {name}
          </Typography>
          <Typography sx={{ color: 'text.secondary' }}>{date}</Typography>
        </AccordionSummary>
        <AccordionDetails>
          <Typography component="span">
            <Stack direction="row">
              {name} {date}
            </Stack>
            
          </Typography>
        </AccordionDetails>
        </Accordion>
    )
}