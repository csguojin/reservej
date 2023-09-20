local seatKey = KEYS[1]
local userKey = KEYS[2]
local startOffset = tonumber(ARGV[1])
local endOffset = tonumber(ARGV[2])

local bitfieldArgs = {}

for i = startOffset, endOffset do
    table.insert(bitfieldArgs, "SET")
    table.insert(bitfieldArgs, "u1")
    table.insert(bitfieldArgs, i)
    table.insert(bitfieldArgs, 0)
end

redis.call('BITFIELD', seatKey, unpack(bitfieldArgs))
redis.call('BITFIELD', userKey, unpack(bitfieldArgs))

return 'OK'