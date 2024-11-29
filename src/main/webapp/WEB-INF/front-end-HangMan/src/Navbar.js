import { Link } from "react-router-dom";

const Navbar = () => {

    return (
        <nav className="nav-bar">
            <a href="/" className="nav-title">
                <h1>Hang Man</h1>
            </a>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/history/log-in">History</Link>
                <Link to="/statistic">Statistic</Link>
            </div>
        </nav>
    );
}

export default Navbar;