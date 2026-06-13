import requests
import time

API_KEY = "sgw_live_T631dGSON_mC7y11vUo8efEr10qwFsG_Ov483-E148w"

URL = "http://localhost:8082/orders/123t"

headers = {
    "X-API-KEY": API_KEY
}

success = 0
failed = 0

for i in range(1, 121):
    try:
        response = requests.get(URL, headers=headers, timeout=5)

        print(
            f"Request {i:03d} | "
            f"Status={response.status_code}"
        )

        if response.status_code == 429:
            print("RATE LIMIT HIT!")
            failed += 1
        else:
            success += 1

    except Exception as ex:
        print(f"Request {i:03d} failed: {ex}")

    time.sleep(0.1)

print("\n===== SUMMARY =====")
print(f"Success: {success}")
print(f"Failed : {failed}")