import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import UserProfileImage from "./UserProfileImage";
import ShoppingCart from "./ShoppingCart";



function NavBar(){
    const [search, setSearch] = useState('');
    const [categories, setCategories] = useState([]);
    const [user, ] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));

    useEffect(() => {
        const getCategories = async () => {
            const categoryData = await fetch('/api/product/category');
            const categoryResponse = await categoryData.json();

            setCategories(categoryResponse);
        }

        getCategories();
    }, []);


    return(
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
                    {user ? 
                        <UserProfileImage user={user} />
                        :
                        <Link to='/login'><button>Login</button></Link>
                    }
                    <ShoppingCart />
                </div>
                
            </div>
            <div className="bottom-part">
                <ul className="categories">
                    <li>Categories: </li>
                    {categories.map(category => <li key={category.categoryName} id={category.id}>{category.categoryName}</li>)}
                </ul>
            </div>
        </nav>
    )
}


export default NavBar;