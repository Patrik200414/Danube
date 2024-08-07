import { Avatar, Button, Divider, MenuItem, MenuList } from "@mui/material";
import styled from "styled-components";
import ShoppingCart from "../../ShoppingCart";
import { Link } from "react-router-dom";

const StyledMenuList = styled(MenuList)({
    backgroundColor: '#2196f3',
    color: '#fff',
    marginTop: '0px'

});

const StyledLink = styled(Link)({
    color: '#fff',
    textDecoration: 'none',
    margin: 'auto'
});

const StyledAvatarContainer = styled('div')({
    margin: 'auto'
});



export default function NavBarMenu({ shoppingCartItemCount, userFirstName, categories }) {
    return (
        <>
            <StyledMenuList>
                <MenuItem>
                    <StyledLink to="/">
                        <h4>Danube</h4>
                    </StyledLink>
                </MenuItem>
                <MenuItem>
                    <StyledAvatarContainer className="additional-information">
                        {userFirstName ?
                            <Link to='/profile'>
                                <Avatar />
                                <p style={{ textAlign: 'center' }}>{userFirstName}</p>
                            </Link>
                            :
                            <Link to='/login'>
                                <Button variant="contained" color="success">Login</Button>
                            </Link>
                        }
                    </StyledAvatarContainer>
                </MenuItem>
                <MenuItem>
                    <ShoppingCart itemCount={shoppingCartItemCount} />
                </MenuItem>
                <Divider />
                <MenuItem>
                    <p>Categories: </p>
                </MenuItem>
                {categories.map(category => (
                    <div key={category.id}>
                        <MenuItem>
                            <h4>{category.categoryName}</h4>
                        </MenuItem>
                        <Divider />
                    </div>
                ))}
            </StyledMenuList>
        </>
    )
}