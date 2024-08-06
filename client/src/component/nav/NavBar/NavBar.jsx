import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import MenuIcon from '@mui/icons-material/Menu';

import { useContext, useEffect, useState } from "react";
import { Link, Outlet } from "react-router-dom";
import { NavbarContext } from "../../../NavbarContext";
import { fetchGet } from "../../../utility/fetchUtilities";
import { Button, List, ListItem, ListItemText } from '@mui/material';
import NavBarMenu from '../NavBarMenu';
import ErrorMessage from '../../ErrorMessage';

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: '3%',
    width: '80%',
  },
}));


const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'inherit',
  width: '100%',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    [theme.breakpoints.up('sm')]: {
      width: '12ch',
      '&:focus': {
        width: '20ch',
      },
    },
  },
}));

const PageTitle = styled(Link)({
  color: 'white',
  textDecoration: 'none'
});

const StyledListItemText = styled(ListItemText)({
  color: 'gray',
  cursor: 'pointer',
});

const StyledList = styled(List)({
  margin: 'auto',
  width: '80%',
  bgcolor: 'background.paper',
  position: 'relative',
  overflow: 'auto',
  maxHeight: 300,
  '& ul': { padding: 0 },
  border: '1px solid gray',
  borderRadius: '5px',
  padding: '0px',
  borderBottom: 'none'
});

const StyledListItem = styled(ListItem)({
  borderBottom: '1px solid gray',
  width: '95%'
});

export default function SearchAppBar() {
  const [search, setSearch] = useState('');
  const [categories, setCategories] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [isMenuOpened, setIsMenuOpened] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navbarInfo = useContext(NavbarContext);

  const checkIfSearchResultEqualsToSearch = searchResults.length === 1 && searchResults[0].subcategoryName.toLowerCase() === search.toLocaleLowerCase();

  useEffect(() => {
    const getCategories = async () => {
      try {
        const categoryResponse = await fetchGet('/api/product/category');
        setCategories(categoryResponse);
      } catch (error) {
        console.log(error.message.errorMessage);
        setErrorMessage(error.message.errorMessage);
      }
    }

    getCategories();
  }, []);

  useEffect(() => {
    const getSearchResult = async () => {
      try{
        const searchResultsData = await fetchGet(`/api/search/product?searchedProductName=${search}`);
        const searchResultData = await searchResultsData.json();
        setSearchResults(searchResultData);
      } catch(error){
        console.log(error.message.errorMessage);
        setErrorMessage(error.message.errorMessage);
      }
      
    }

    if (search.length > 0) {
      getSearchResult();
    } else {
      setSearchResults([]);
    }

  }, [search])


  return (
    <>
      {errorMessage && <ErrorMessage error={errorMessage}/>}
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static">
          <Toolbar>
            <IconButton
              size="large"
              edge="start"
              color="inherit"
              aria-label="open drawer"
              sx={{ mr: 2 }}
              onClick={() => setIsMenuOpened(prev => !prev)}
            >
              <MenuIcon />
            </IconButton>
            <PageTitle to="/">
              <Typography
                variant="h6"
                noWrap
                component="div"
                sx={{ flexGrow: 1, display: { xs: 'none', sm: 'block' } }}
              >
                Danube
              </Typography>
            </PageTitle>
            <Search>
              <StyledInputBase
                name="search"
                id="search"
                placeholder="Search Danube..."
                inputProps={{ 'aria-label': 'search' }}
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </Search>
            <Link to={`/product/${search}`}>
              <Button variant="contained" color="warning">Search</Button>
            </Link>
          </Toolbar>
        </AppBar>
        {
          !checkIfSearchResultEqualsToSearch
          &&
          <StyledList
            subheader={<li />}
          >
            {searchResults.map((searchResult, i) => (
              <StyledListItem key={`${searchResult.productName}-${i}`}>
                <StyledListItemText
                  onClick={() => setSearch(searchResult.subcategoryName)}
                  primary={searchResult.subcategoryName}
                />
              </StyledListItem>
            ))}
          </StyledList>
        }
        {
          isMenuOpened
          &&
          <NavBarMenu
            shoppingCartItemCount={navbarInfo.cartItemNumber}
            userFirstName={navbarInfo.userFirstName}
            categories={categories}
          />
        }
      </Box>
      <Outlet />
    </>
  );
}