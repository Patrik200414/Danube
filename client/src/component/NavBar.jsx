import { useContext, useEffect, useState } from "react";
import { Link, Outlet } from "react-router-dom";
import UserProfileImage from "./user/UserProfileImage";
import ShoppingCart from "./ShoppingCart";
import {NavbarContext} from "../NavbarContext";
import fetchGet from "../utility/fetchGet";




function NavBar(){
    const [search, setSearch] = useState('');
    const [categories, setCategories] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const navbarInfo = useContext(NavbarContext);

    useEffect(() => {
        const getCategories = async () => {
            const categoryData = await fetch('/api/product/category');
            const categoryResponse = await categoryData.json();

            setCategories(categoryResponse);
        }

        getCategories();
    }, []);

    useEffect(() => {
        const getSearchResult = async () => {
            const searchResultsData = await fetchGet(`/api/search/product?searchedProductName=${search}`);
            const searchResultData = await searchResultsData.json();
            setSearchResults(searchResultData);
        }

        if(search.length > 2){
            getSearchResult();
        } else{
            setSearchResults([]);
        }

    }, [search])

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
                        <div className="search-holder">
                            <input type="text" name="search" id="search" placeholder="Search Danube..." value={search} onChange={e => setSearch(e.target.value)}/>
                            {searchResults.length > 0 && 
                                <ul className="search-result-container">
                                    {searchResults.map((searchResult, i) => <li key={`${searchResult.productName}-${i}`} className="searchResult">{searchResult.productName}</li>)}
                                </ul>
                            }
                        </div>
                        <button>Search</button>
                    </div>
                    <div className="additional-information">
                        {navbarInfo.userFirstName ? 
                            <UserProfileImage userFirstName={navbarInfo.userFirstName} />
                            :
                            <Link to='/login'><button>Login</button></Link>
                        }
                        <ShoppingCart itemCount={navbarInfo.cartItemNumber}/>
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