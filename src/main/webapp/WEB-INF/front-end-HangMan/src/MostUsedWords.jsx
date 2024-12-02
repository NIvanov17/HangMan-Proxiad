import { useEffect, useState } from "react";


const MostUsedWords = () => {

    const [gamesData, setGamesData] = useState({ word: [], avgAttempts: 0, winLossRatio: "0/0" });

    useEffect(() => {
        fetch('http://localhost:8080/api/v1/games/statistic')
            .then(res => res.json())
            .then(data => {
                console.log(data);
                setGamesData(data)
            })
    }, []);

    return (
        <div className="most-usedWords">
            <h2>Top 10 Most Used Words</h2>
            <table>
                <thead>
                    <tr>
                        <th>Number</th>
                        <th>Word</th>
                    </tr>
                </thead>
                <tbody>
                    {gamesData.word.map((word, index) => (
                        <tr key={index}>
                            <td>{++index}</td>
                            <td>{word}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="attempts">
                <h2>
                    Average Attempts per Game: <span className="attempts-number">{gamesData.avgAttempts}</span>
                </h2>
            </div>
            <div className="ratio">
                <h2>Win/Loss Ratio:</h2>
                <p>{gamesData.winLossRatio}</p>
            </div>
        </div>
    );
}

export default MostUsedWords;