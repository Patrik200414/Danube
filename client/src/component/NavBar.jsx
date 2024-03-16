import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import userImage from "../static/userImage.svg";


function NavBar(){
    const [search, setSearch] = useState('');
    const [categories, setCategories] = useState([]);
    const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('USER_JWT')));

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
                <h1 className="logo">Danube</h1>
                <div className="input-container">
                    <input type="text" name="search" id="search" placeholder="Search Danube..." value={search} onChange={e => setSearch(e.target.value)}/>
                    <button>Search</button>
                </div>
                <div className="additional-information">
                    {user ? 
                        <Link to='/profile'>
                            <div className="greeting">
                                <img className="user-image" src={userImage} />
                                <div>
                                    <h5>Welcome</h5>
                                    <p>{user.firstName}</p>
                                </div>
                            </div>
                        </Link>
                        :
                        <Link to='/login'><button>Login</button></Link>
                    }
                </div>
                
            </div>
            <div className="bottom-part">
                <ul className="categories">
                    <li>Categories: </li>
                    {categories.map(category => <li key={category}>{category}</li>)}
                </ul>
            </div>
        </nav>
    )
}


export default NavBar;