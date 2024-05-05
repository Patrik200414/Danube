import { createContext } from "react";
import PropTypes from 'prop-types';

const NavbarContext = createContext();


function NavbarProvider({children, navbarInformation}){
    return(
        <NavbarContext.Provider value={navbarInformation}>
            {children}
        </NavbarContext.Provider>
    )
}

NavbarProvider.propTypes = {
    children: PropTypes.element
}

NavbarProvider.propTypes = {
    navbarInformation: PropTypes.object
}

export {NavbarProvider, NavbarContext};