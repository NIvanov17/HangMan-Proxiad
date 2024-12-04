import { useEffect, useState } from "react";
import "./Ranking.css";
import Loader from "../../Components/Loader/Loader";
import Error from './../Errors/Error';


const TopTenRankings = () => {

    const [playerData, setPlayerData] = useState([]);
    const [isPending, setIsPending] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        setIsPending(true);
        fetch('http://localhost:8080/api/v1/players/ranking/top-ten')
            .then(res => res.json())
            .then((data) => {
                setIsPending(false);
                setPlayerData(data);
            }).catch(err => setError(err))
    }, [])

    if (error) {
        return <Error error={error} />;
    }
    return (
        <div className="ranking">
            {
                isPending ? (
                    <div className="loader-container">
                        <Loader />
                    </div>
                ) : (
                    <>
                        <h2>Top 10 Players</h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>Player ID</th>
                                    <th>Username</th>
                                    <th>Total Wins</th>
                                </tr>
                            </thead>
                            <tbody>
                                {
                                    playerData.map((player, index) => (
                                        <tr key={index}>
                                            <td>{++index}</td>
                                            <td>{player.username}</td>
                                            <td>{player.totalWins}</td>
                                        </tr>
                                    ))
                                }
                            </tbody>
                        </table>
                    </>
                )}
        </div>
    );
}

export default TopTenRankings;