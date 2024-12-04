import { useNavigate } from "react-router-dom";
import "./Error.css";


const Error = ({ error }) => {

    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/');
    }
    const getErrorMessage = () => {
        return error?.message || "Oooppsss... Something went wrong!"
    }

    return (
        <div className="error">
            <h1>Congratulations, you broke it! :D</h1>
            <p>{getErrorMessage()}</p>
            <form onSubmit={handleClick}>
                <button id="homeButton" type="submit" name="toHomePage">Home Page</button>
            </form>
        </div>
    );
}

export default Error;