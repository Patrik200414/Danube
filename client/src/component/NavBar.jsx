import { useContext, useEffect, useState } from "react";
import { Link, Outlet } from "react-router-dom";
import UserProfileImage from "./user/UserProfileImage";
import ShoppingCart from "./ShoppingCart";
import {NavbarContext} from "../NavbarContext";




function NavBar(){
    const [search, setSearch] = useState('');
    const [categories, setCategories] = useState([]);
    const navbarInfo = useContext(NavbarContext);

    useEffect(() => {
        const getCategories = async () => {
            const categoryData = await fetch('/api/product/category');
            const categoryResponse = await categoryData.json();

            setCategories(categoryResponse);
        }

        getCategories();
    }, []);
    /* console.log(navbarInfo); */

    return(
        <>
        {navbarInfo && 
            <>
            <nav className="nav-bar">
                <div className="top-part">
                    <h1 className="logo">
                        <Link to='/'>Danube</Link>    
                    </h1>
                    <div className="input-container">
                        <input type="text" name="search" id="search" placeholder="Search Danube..." value={search} onChange={e => setSearch(e.target.value)}/>
                        <button>Search</button>
                    </div>
                    <div className="additional-information">
                        {navbarInfo.userFirstName ? 
                            <UserProfileImage userFirstName={navbarInfo.userFirstName} />
                            :
                            <Link to='/login'><button>Login</button></Link>
                        }
                        <ShoppingCart itemCount={navbarInfo.cartItems.length}/>
                    </div>
                    
                </div>
                <div className="bottom-part">
                    <ul className="categories">
                        <li>Categories: </li>
                        {categories.map(category => <li key={category.categoryName} id={category.id}>{category.categoryName}</li>)}
                    </ul>
                </div>
            </nav>
            <Outlet />
            </>
        }
        </>
    )
}


export default NavBar;