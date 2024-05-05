import { createContext } from "react";
import getNavbarInformation from "./utility/getNavbarInformation";
import PropTypes from 'prop-types';

const NavbarContext = createContext();


function NavbarProvider({children}){
    const navbarInfo = getNavbarInformation();
    return(
        <NavbarContext.Provider value={navbarInfo}>
            {children}
        </NavbarContext.Provider>
    )
}

NavbarProvider.propTypes = {
    children: PropTypes.element
}

export {NavbarProvider, NavbarContext};