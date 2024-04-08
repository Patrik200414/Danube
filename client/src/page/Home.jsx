import NavBar from "../component/NavBar";
import ProductContainer from "../component/ProductContainer";

function Home(){
    return(
        <div className="home">
            <NavBar />
            <ProductContainer />
        </div>
    )
}

export default Home;