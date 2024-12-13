import { Link } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {

    return (
        <nav className="nav-bar">
            <a href="/" className="nav-title">
                <h1>Hang Man</h1>
            </a>
            <div className="links">
                <Link to="/">Home</Link>
                <Link to="/history/log-in">History</Link>
                <Link to="/statistics">Statistics</Link>
                <Link to="/rankings">Rankings</Link>
                <Link to="/admin">Admin</Link>
                <Link to="/register" className="btn">Register</Link>
                <Link to="/login" className="btn">Log In</Link>
                <Link to="/logout" className="btn">Log Out</Link>
            </div>
        </nav>
    );
}

export default Navbar;